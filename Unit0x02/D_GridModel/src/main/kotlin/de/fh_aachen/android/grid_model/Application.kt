// (C) 2025 A.Voß, a.voss@fh-aachen.de, info@codebasedlearning.dev

package de.fh_aachen.android.grid_model

import android.app.Application
import de.fh_aachen.android.grid_model.model.AppModel

class GridModelApplication : Application() {
    // => This is not an optimal solution nor best practise.
    // Your task: Reorganise your data according to the Android architecture,
    // use viewModels.
    companion object {
        val appModel = AppModel()
    }
}
