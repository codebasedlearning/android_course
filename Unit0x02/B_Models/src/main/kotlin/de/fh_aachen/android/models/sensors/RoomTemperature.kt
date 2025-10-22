// (C) 2025 A.Voß, a.voss@fh-aachen.de, info@codebasedlearning.dev

package de.fh_aachen.android.models.sensors

import android.util.Log
import androidx.compose.runtime.asIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fh_aachen.android.models.ModelsApplication
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val roomTemperatureRange: ClosedRange<Int> = 15..35

class RoomTemperatureRepository() : SensorValues {
    override var rawValue: Int = roomTemperatureRange.center()
        private set

    override var bias: Int = 0
        private set

    override val calibratedValue: Int
        get() = rawValue + bias

    override fun fetch() {
        rawValue = rawValue.nextJitter(withinRange = roomTemperatureRange) //     roomTemperatureSimulation(rawValue)
        Log.i(SENSOR_TAG,"new room temperature value: $calibratedValue ($rawValue;$bias)")
    }

    override fun calibrate(newBias: Int) {
        bias = newBias
        Log.i(SENSOR_TAG,"new room temperature calibration: $calibratedValue ($rawValue;$bias)")
    }
}

open class RoomTemperatureViewModel() : ViewModel() {
    private val repository = ModelsApplication.serviceLocator.roomTemperatureRepository

    // UDF: data towards view (via subscribers)
    private val _rawValue = mutableIntStateOf(repository.calibratedValue)
    val rawValue = _rawValue.asIntState()

    private val _calibratedValue = mutableIntStateOf(repository.calibratedValue)
    val calibratedValue = _calibratedValue.asIntState()
    private val _bias = mutableIntStateOf(repository.bias)
    val bias = _bias.asIntState()

    fun update() {
        _rawValue.intValue = repository.rawValue
        _calibratedValue.intValue = repository.calibratedValue
        _bias.intValue = repository.bias
    }

    // UDF: event towards model (repository)
    fun calibrate(incBias: Int) {
        repository.calibrate(repository.bias + incBias)
        update()
    }
}

class RoomTemperatureRefreshingViewModel() : RoomTemperatureViewModel() {
    init {
        startUpdating()
    }

    private fun startUpdating() {
        viewModelScope.launch {
            while (true) {
                update()
                delay(1000L)
            }
        }
    }
}
