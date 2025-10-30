// (C) 2025 A.Voß, a.voss@fh-aachen.de, info@codebasedlearning.dev

package de.fh_aachen.android.models

import android.app.Application
import de.fh_aachen.android.models.sensors.CityTemperatureRepository
import de.fh_aachen.android.models.sensors.DeviceTemperatureRepository
import de.fh_aachen.android.models.sensors.RoomTemperatureRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

data class ServiceLocator(
    val roomTemperatureRepository: RoomTemperatureRepository,
    val deviceTemperatureRepository: DeviceTemperatureRepository,
    val cityTemperatureRepository: CityTemperatureRepository
)

class ModelsApplication : Application() {
    companion object {
        private lateinit var instance: ModelsApplication

        val serviceLocator by lazy {
            ServiceLocator(
                instance.roomTemperatureRepository,
                instance.deviceTemperatureRepository,
                instance.cityTemperatureRepository
            )
        }
    }

    // SupervisorJob grants that in case of an error other child coroutines are not cancelled.
    private val appScope: CoroutineScope by lazy { CoroutineScope(SupervisorJob() + Dispatchers.Default) }

    private val roomTemperatureRepository by lazy { RoomTemperatureRepository() }
    private val deviceTemperatureRepository by lazy { DeviceTemperatureRepository(appScope) }
    private val cityTemperatureRepository by lazy { CityTemperatureRepository(appScope) }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
