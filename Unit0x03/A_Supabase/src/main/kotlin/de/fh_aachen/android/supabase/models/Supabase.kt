package de.fh_aachen.android.supabase.models

import android.util.Log
import de.fh_aachen.android.supabase.BuildConfig

import androidx.lifecycle.ViewModel
import de.fh_aachen.android.supabase.SupabaseApplication
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.realtime.broadcastFlow
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.realtime
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.time.Duration.Companion.seconds

private const val TAG = "Supabase"

@kotlinx.serialization.Serializable
data class Product(
    val id: Int,
    val name: String,
    val category: Category? = null,
    val unit: String,
    val price: Double,
    val vat: Double
)

@kotlinx.serialization.Serializable
data class Category(
    val id: Int,
    val name: String
)

@kotlinx.serialization.Serializable
data class Message(val msg: String, val no: Int)

class SupabaseRepository(private val scope: CoroutineScope) {

    // both functions do not throw exceptions, no network traffic is performed

    private val supabase: SupabaseClient = createSupabaseClient(
        supabaseUrl = "https://${BuildConfig.supabase_url}",
        supabaseKey = BuildConfig.supabase_anon_key
    ) {
        install(Auth)
        install(Postgrest)
        install(Realtime) { reconnectDelay = 5.seconds }
        httpEngine = OkHttp.create()
    }

    private val ami_zone_channel = supabase.realtime.channel("ami_zone_channel")
    private val event_name = "my_event"

    private val _messageFlow = MutableStateFlow(Message("-",0))
    private var broadcastCounter = 1

    // read-only flow
    val messageFlow: StateFlow<Message> get() = _messageFlow

    init {
        Log.v(TAG, "supabase sync start")

        try {
            scope.launch {
                supabase.auth.sessionStatus
                    .onEach { state ->
                        when (state) {
                            is SessionStatus.NotAuthenticated -> Log.i(TAG, "supabase session not authenticated")
                            is SessionStatus.Authenticated -> Log.i(TAG, "supabase session authenticated")
                            SessionStatus.Initializing -> Log.i(TAG, "supabase session initializing")
                            is SessionStatus.RefreshFailure -> Log.i(TAG, "supabase session refresh failure")
                        }
                    }
                    .collect { value -> println("Collected $value") }
            }

            scope.launch {
                ami_zone_channel.broadcastFlow<Message>(event_name)
                    .onStart { ami_zone_channel.subscribe(blockUntilSubscribed = true) }
                    .onEach { _messageFlow.value = it }
                    .onCompletion { runCatching { ami_zone_channel.unsubscribe() } }
                    .launchIn(scope)

                /* shorter than:
                val broadcastFlow = ami_zone_channel.broadcastFlow<Message>(event = "my_event")  // get flow before subscribe
                ami_zone_channel.subscribe(blockUntilSubscribed = true)
                broadcastFlow.collect { incoming ->
                    Log.v(TAG, "supabase broadcast <- $incoming")
                    _messageFlow.value = incoming
                }
                */
            }

        } catch (e: Exception) {
            Log.e(TAG, "supabase init failed", e)
        }
    }

    fun ping() {
        Log.e(TAG, "supabase ping")
        scope.launch {
            try {
                ami_zone_channel.broadcast(event_name, message = buildJsonObject {
                    put("msg", "HuHu Python")
                    put("no", ++broadcastCounter)
                })
            } catch (e: Exception) {
                Log.e(TAG, "supabase ping failed", e)
            }
        }
    }

    fun printProducts() {
        Log.e(TAG, "supabase print products")
        scope.launch {
            try {
                var user = supabase.auth.currentUserOrNull()
                if (user == null) {
                    supabase.auth.signInWith(Email) {
                        email = BuildConfig.supabase_user_email
                        password = BuildConfig.supabase_user_password
                    }
                    user = supabase.auth.currentUserOrNull()
                }
                Log.v(TAG, "supabase user: ${user?.id ?: "no user"}")

                val data = supabase
                    .from(schema="ami_zone", table="shop_product")
                    .select(Columns.raw("id,name,category:shop_category(*),unit,price,vat"))

                val products: List<Product> = data.decodeList<Product>()
                products.take(5).forEach { product ->
                    Log.v(TAG, "supabase prod: ${product}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "supabase ping failed", e)
            }
        }
    }
}

class SupabaseViewModel : ViewModel() {
    private val repository = SupabaseApplication.serviceLocator.supabaseRepository
    val msg: StateFlow<Message> = repository.messageFlow

    fun ping() = repository.ping()
    fun printPoducts() = repository.printProducts()
}
