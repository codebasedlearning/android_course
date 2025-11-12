// (C) 2025 A.Voß, a.voss@fh-aachen.de, info@codebasedlearning.dev

package de.fh_aachen.android.models_with_hilt

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import javax.inject.Singleton

/*
 * see https://developer.android.com/training/dependency-injection
 *
 * Hilt is a dependency injection (DI) framework built on top of Dagger, made specifically
 * for Android:
 *    - Creates and wires up objects for you (via dependency graphs).
 *    - Manages scopes automatically (Application, Activity, Fragment, ViewModel, etc.).
 *    - Integrates tightly with Android lifecycle (so injected objects live as long as their owners).
 *    - Removes 95% of Dagger’s ceremony.
 *
 * How it works:
 *    - You annotate classes to tell Hilt what they provide (@Module, @Provides, @Binds).
 *    - You annotate consumers to tell Hilt where to inject (@AndroidEntryPoint, @Inject, etc.).
 *    - At compile time, Hilt generates all the Dagger machinery (components, subcomponents,
 *      factories).
 *    - At runtime, Android + Hilt handle initialization and injection for you.
 *
 * Instead of 'new CityTemperatureRepository()', you just say 'I need one' and Hilt supplies
 * it — correctly scoped.
 *
 * Key annotations:
 *
 *    - @HiltAndroidApp on your Application subclass
 *      Tells Hilt: start dependency injection here. Generates the root component that survives the whole app.
 *      Here: on ModelsWithHiltApplication
 *
 *    - @AndroidEntryPoint on an Activity, Fragment, Service, or View
 *      Marks an Android component as injectable. Hilt will inject all @Inject fields/constructors
 *      there automatically.
 *      Here: on MainActivity
 *
 *    - @HiltViewModel on a ViewModel class
 *      Tells Hilt to manage this ViewModel and inject its dependencies. Must have an @Inject constructor.
 *      Here: on CityTemperatureViewModel
 *
 *    - @Inject on a constructor or field
 *      Says: Provide this dependency. On constructors, it means 'you can build this automatically.'
 *      Here: on CityTemperatureRepository and CityTemperatureViewModel
 *
 *    - @Module on a class that groups @Provides or @Binds methods
 *      Declares how to build or map dependencies Hilt can’t auto-create.
 *      Here: on AppModule
 *
 *    - @Provides on a function inside a @Module
 *    - Supplies a dependency manually (a factory method). Used when you can’t use @Inject constructor
 *      (e.g., third-party classes).
 *      Here: on provideSimulationCoroutineScope()
 *
 *    - @Binds on an abstract function inside a @Module
 *      Connects an interface to its implementation (Repo -> RepoImpl). Lighter than @Provides
 *      but requires an existing @Inject constructor.
 *      Here: not used
 *
 *    - @InstallIn(...) on a @Module
 *      Tells Hilt which component (scope) this module belongs to — e.g. SingletonComponent,
 *      ActivityComponent, ViewModelComponent, etc.
 *      Here: on AppModule
 *
 *    - Scope annotations (@Singleton, @ActivityRetainedScoped, etc.) on classes or providers
 *      Controls lifetime: one per app, per activity, per viewmodel, etc.
 *      Here: on CityTemperatureRepository and provideSimulationCoroutineScope
 *
 * Goal:
 *    - Inject into Application -> @HiltAndroidApp
 *    - Inject into Activity/Fragment/View -> @AndroidEntryPoint
 *    - Inject dependencies into a class -> @Inject (constructor or field)
 *    - Provide third-party objects -> @Module + @Provides
 *    - Bind interface to implementation -> @Binds
 *    - Manage ViewModels -> @HiltViewModel + @Inject
 *    - Control lifetime -> use scope annotation (@Singleton, etc.)
 *
 */

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideSimulationCoroutineScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }
}

@HiltAndroidApp
class ModelsWithHiltApplication : Application() {
    @Inject lateinit var appScope: CoroutineScope
}
