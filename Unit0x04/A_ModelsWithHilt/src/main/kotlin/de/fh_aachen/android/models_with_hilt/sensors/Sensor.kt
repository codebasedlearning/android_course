// (C) 2025 A.Voß, a.voss@fh-aachen.de, info@codebasedlearning.dev

package de.fh_aachen.android.models_with_hilt.sensors

import kotlinx.coroutines.flow.StateFlow

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

