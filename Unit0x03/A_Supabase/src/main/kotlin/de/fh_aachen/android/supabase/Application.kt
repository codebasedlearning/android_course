// (C) 2025 A.Voß, a.voss@fh-aachen.de, info@codebasedlearning.dev

package de.fh_aachen.android.supabase

import android.app.Application
import de.fh_aachen.android.supabase.models.SupabaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

data class ServiceLocator(
    val supabaseRepository: SupabaseRepository,
)

class SupabaseApplication : Application() {
    companion object {
        private lateinit var instance: SupabaseApplication

        val serviceLocator by lazy {
            ServiceLocator(
                instance.supabaseRepository,
            )
        }
    }

    // SupervisorJob grants that in case of an error other child coroutines are not cancelled.
    private val appScope: CoroutineScope by lazy { CoroutineScope(SupervisorJob() + Dispatchers.Default) }

    private val supabaseRepository by lazy { SupabaseRepository(appScope) }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
