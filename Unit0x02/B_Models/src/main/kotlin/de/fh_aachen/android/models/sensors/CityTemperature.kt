// (C) 2025 A.Voß, a.voss@fh-aachen.de, info@codebasedlearning.dev

package de.fh_aachen.android.models.sensors

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fh_aachen.android.models.ModelsApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private val cityTemperatureRange: ClosedRange<Int> = -5..15

/*
Here we have a combined flow. Otherwise it is similar to the Device-case.
*/

class CityTemperatureRepository(private val simulationScope: CoroutineScope): SensorFlow {
    private val _data = MutableStateFlow(SensorData(rawValue = cityTemperatureRange.center(), bias = 0))
    override val data: StateFlow<SensorData> get() = _data

    init {
        startUpdating()
    }

    private fun startUpdating() {
        simulationScope.launch {
            while (true) {
                _data.value = SensorData(rawValue = _data.value.rawValue.nextJitter(withinRange = cityTemperatureRange), bias = _data.value.bias)
                Log.i(SENSOR_TAG,"new device temperature value: ${_data.value.calibratedValue} (${_data.value.rawValue};${_data.value.bias})")
                delay(1000L)
            }
        }
    }

    override fun calibrate(newBias: Int) {
        _data.value = SensorData(rawValue = _data.value.rawValue, bias = newBias)
    }
}

class CityTemperatureViewModel : ViewModel() {
    private val repository = ModelsApplication.serviceLocator.cityTemperatureRepository

    val data: StateFlow<SensorData> = repository.data

    // We need to extract single dimensions out of the flow, that is what map is for, but
    // after map we have a simple flow and .stateIn transfers it back to a StateFlow,
    // so we are required to say where to store the value and with which initial value

    val rawValue: StateFlow<Int> = data.map { it.rawValue }
        .stateIn(viewModelScope, SharingStarted.Eagerly, repository.data.value.rawValue)
    val calibratedValue: StateFlow<Int> = data.map { it.calibratedValue }
        .stateIn(viewModelScope, SharingStarted.Eagerly, repository.data.value.calibratedValue)
    val bias: StateFlow<Int> = data.map { it.bias }
        .stateIn(viewModelScope, SharingStarted.Eagerly, repository.data.value.bias)

    fun calibrate(incBias: Int) {
        repository.calibrate(repository.data.value.bias + incBias)
        // update() not needed
    }

    // Use combine and add .stateIn to combine flows if you need to.
}
