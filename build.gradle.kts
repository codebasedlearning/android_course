// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // Don’t actually apply these plugins to the modules, just make it available.
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    // alias(libs.plugins.android.conventions) apply false
}
