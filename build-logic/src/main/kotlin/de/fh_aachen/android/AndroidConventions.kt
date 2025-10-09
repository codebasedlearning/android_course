package de.fh_aachen.android

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidConventions : Plugin<Project> {
    override fun apply(project: Project) {

        // from gradle.properties
        fun intProp(key: String, default: Int) =
            (project.findProperty(key) ?: project.rootProject.findProperty(key) ?: default)
                .toString().toInt()

        // for all projects and libs set these
        val compileSdk = intProp("android.compileSdk", 36)
        val minSdk     = intProp("android.minSdk", 27)
        val targetSdk  = intProp("android.targetSdk", compileSdk)

        project.plugins.withId("com.android.application") {
            project.extensions.configure<ApplicationExtension> {
                this.compileSdk = compileSdk
                defaultConfig {
                    this.minSdk = minSdk
                    this.targetSdk = targetSdk
                }
            }
        }
        project.plugins.withId("com.android.library") {
            project.extensions.configure<LibraryExtension> {
                this.compileSdk = compileSdk
                defaultConfig {
                    this.minSdk = minSdk
                }
            }
        }
    }
}
