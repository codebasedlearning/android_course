// (C) 2025 A.Voß, a.voss@fh-aachen.de, info@codebasedlearning.dev

package de.fh_aachen.android.models_with_hilt.sensors

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

const val TAG = "CITY"

private val cityTemperatureRange: ClosedRange<Int> = -5..15

/* instead of
    class CityTemperatureRepository(
        private val simulationScope: CoroutineScope
    ): SensorFlow { ...

    use @Singleton and @Inject
 */

@Singleton
class CityTemperatureRepository @Inject constructor(
    private val simulationScope: CoroutineScope
) : SensorFlow {
    private val _data = MutableStateFlow(SensorData(rawValue = cityTemperatureRange.center(), bias = 0))
    override val data: StateFlow<SensorData> get() = _data

    init {
        startUpdating()
        Log.i(TAG, "CityTemperatureRepository created")
    }

    private fun startUpdating() {
        simulationScope.launch {
            while (true) {
                _data.value = SensorData(rawValue = _data.value.rawValue.nextJitter(withinRange = cityTemperatureRange), bias = _data.value.bias)
                delay(1000L)
            }
        }
    }

    override fun calibrate(newBias: Int) {
        _data.value = SensorData(rawValue = _data.value.rawValue, bias = newBias)
    }
}

/* instead of
    class CityTemperatureViewModel : ViewModel() {
        private val repository = ModelsApplication.serviceLocator.cityTemperatureRepository

    use @HiltViewModel and @Inject
 */

@HiltViewModel
class CityTemperatureViewModel @Inject constructor(
    private val repository: CityTemperatureRepository
) : ViewModel() {
    val data: StateFlow<SensorData> = repository.data
    val calibratedValue: StateFlow<Int> = data.map { it.calibratedValue }
        .stateIn(viewModelScope, SharingStarted.Eagerly, repository.data.value.calibratedValue)
    val bias: StateFlow<Int> = data.map { it.bias }
        .stateIn(viewModelScope, SharingStarted.Eagerly, repository.data.value.bias)

    init {
        Log.i(TAG, "CityTemperatureViewModel created")
    }

    fun calibrate(incBias: Int) {
        repository.calibrate(repository.data.value.bias + incBias)
    }
}
