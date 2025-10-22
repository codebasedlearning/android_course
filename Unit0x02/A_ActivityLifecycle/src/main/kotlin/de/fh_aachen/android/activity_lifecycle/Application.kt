// (C) 2025 A.Voß, a.voss@fh-aachen.de, info@codebasedlearning.dev

package de.fh_aachen.android.activity_lifecycle

import android.app.Application
import android.util.Log
import de.fh_aachen.android.activity_lifecycle.models.InputDataModel


// Do not forget to add this to the AndroidManifest.xml file ('android:name').

class ActivityLifecycleApplication : Application() {
    // - this is actually a singleton, and its lifecycle is the application
    // - access via ActivityLifecycleApplication.instance
    // => more on this in B_Models
    companion object {
        lateinit var instance: ActivityLifecycleApplication
    }

    val inputModel = InputDataModel()

    override fun onCreate() {
        super.onCreate()
        Log.i("APP","init Activity Lifecycle Application")
        instance = this
    }

    // override fun onTerminate()
}
