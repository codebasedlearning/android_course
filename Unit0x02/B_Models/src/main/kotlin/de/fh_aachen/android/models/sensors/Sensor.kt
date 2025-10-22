// (C) 2025 A.Voß, a.voss@fh-aachen.de, info@codebasedlearning.dev

package de.fh_aachen.android.models.sensors

import kotlinx.coroutines.flow.StateFlow

const val SENSOR_TAG = "Sensor"

// three 'individual' values: raw, bias, and calibrated
// mode: poll sensor
//  - ViewModel 1: refresh and poll
//  - ViewModel 2: refresh and poll regularly
interface SensorValues {
    val rawValue: Int
    val bias: Int
    val calibratedValue: Int

    fun fetch()
    fun calibrate(newBias: Int)
}

// three 'individual' flows
// mode: subscribe to flow
interface SensorFlows {
    val rawValue: StateFlow<Int>
    val bias: StateFlow<Int>
    val calibratedValue: StateFlow<Int>

    fun calibrate(newBias: Int)
}

data class SensorData(val rawValue: Int, val bias: Int) {
    val calibratedValue: Int
        get() = rawValue + bias
}

interface SensorFlow {
    val data: StateFlow<SensorData>
    fun calibrate(newBias: Int)
}

fun ClosedRange<Int>.center() = start + (endInclusive - start) / 2
fun Int.nextJitter(withinRange: ClosedRange<Int>) = (this + (-2..2).random()).coerceIn(withinRange)
