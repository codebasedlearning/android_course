package de.fh_aachen.android.models_with_hilt.sensors

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent

private const val TAG = "INJECT"

/*
 * To get an instance in the app, you need:
 *    - @HiltAndroidApp on your Application
 *    - @AndroidEntryPoint on the consumer (Activity/Fragment/etc.)
 *    - @Inject on the class you want the instance
 *
 * Build
 *  The constructor needs type => What provider do I have that returns this type?
 *
 * @Inject tells Dagger/Hilt that this class is constructable automatically
 * by the DI framework.
 *
 * In terms of pure Kotlin semantics this class is identical to
 *      class InjectMe {...
 */

class InjectMe @Inject constructor() {
    init {
        Log.i(TAG,"init InjectMe")
    }
    fun print() {
        Log.i(TAG,"print InjectMe")
    }
}

// or as singleton

@Singleton
class InjectOnce @Inject constructor() {
    init {
        Log.i(TAG,"init InjectOnce")
    }
    fun print() {
        Log.i(TAG,"print InjectOnce")
    }
}

/*
 * We have two different worlds inside Hilt:
 *    - World A: 'Hilt can construct this class itself' => @Inject constructor on your own class
 *    - World B: 'Hilt can’t construct this, I have to tell it how' => @Module + @Provides
 *
 * Build rules
 *    - Class has an @Inject constructor => No module needed
 *    - Class has no @Inject constructor => Use @Module + @Provides
 *    - You want a singleton => Add @Singleton and install the module into SingletonComponent
 *    - You want a scoped instance => Use the matching component + scope annotation
 *    - You want a fresh new instance every time => No scope annotation
 *
 * Scopes
 * A scope says: 'Create one instance of this class per X, and reuse it every time
 * someone in that X asks for it.', where X is:
 *    - the whole app
 *    - an Activity
 *    - a ViewModel
 *    etc.
 * If you don’t add a scope, you get a new instance every injection.
 *
 * Activity scope => One instance per Activity instance (does not survive rotation).
 * ViewModel scope => One instance per ViewModel instance (only works for classes that Hilt can construct).
 * Singleton scope => One instance per Application.
 */

class InjectLegacy {
    init {
        Log.i(TAG,"init InjectLegacy")
    }
    fun print() {
        Log.i(TAG,"print InjectLegacy")
    }
}

/*
 * @Module:    This class contains DI instructions for Hilt/Dagger.
 * @InstallIn: We tell Hilt which component / lifetime bucket the module’s bindings go to.
 */

@Module
@InstallIn(SingletonComponent::class)
// @InstallIn(ActivityComponent::class)
// does not work with InjectLegacy, just to be complete @InstallIn(ViewModelComponent::class)
object AppModule {

    @Provides
    // @Singleton
    // @ActivityScoped
    // @ViewModelScoped
    fun provideInjectLegacy(): InjectLegacy {
        Log.i(TAG,"provide InjectLegacy")
        return InjectLegacy()
    }
}
