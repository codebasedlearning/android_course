// (C) 2025 A.Voß, a.voss@fh-aachen.de, info@codebasedlearning.dev

package de.fh_aachen.android.grid_model.model

import androidx.compose.runtime.toMutableStateList
import de.fh_aachen.android.grid_model.R

class AppModel {
    var userName = "N.N."

    val dim = 3
    val clicks = List(dim*dim) { 0 }.toMutableStateList()

    fun cardTextOf(index: Int) = when (clicks[index]) {
            1,3 -> "O"
            2,4 -> "X"
            else -> ""
        }

    fun cardImageOf(index: Int) =
        when (clicks[index]) {
            1 -> R.drawable.tile_knight
            2 -> R.drawable.tile_builder
            3 -> R.drawable.tile_doctor
            4 -> R.drawable.tile_professor
            else -> R.drawable.tile_background3
        }

    fun isClicked(index: Int) {
       clicks[index] = (clicks[index] + 1) % 5
    }
}
