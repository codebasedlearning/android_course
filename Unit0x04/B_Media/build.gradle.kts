plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("android-conventions")

    //alias(libs.plugins.kotlin.kapt)
    //id("com.google.devtools.ksp").version("1.6.10-1.0.4")
    //id("com.google.devtools.ksp")
    //kotlin("kapt")
}
//https://developer.android.com/build/migrate-to-ksp
android {
    namespace = "de.fh_aachen.android.media"
    //compileSdk = 35

    defaultConfig {
        applicationId = "de.fh_aachen.android.media"
        //minSdk = 27
        //targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_11
//        targetCompatibility = JavaVersion.VERSION_11
//    }
    kotlin {
        jvmToolchain(21)
    }
//    kotlinOptions {
//        jvmTarget = "11"
//    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)

    //implementation(libs.exoplayer)
    //implementation(libs.exoplayer.ui)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.exoplayer.dash)
    implementation(libs.androidx.media3.ui)

    //implementation(libs.moshi)

    // Moshi Kotlin extension (for better Kotlin support)
    //implementation(libs.moshi.kotlin)

    // Moshi code generation (Kotlin codegen for automatic adapters)
    //kapt("com.squareup.moshi:moshi-kotlin-codegen:1.14.0")
    //ksp("com.squareup.moshi:moshi-kotlin-codegen:1.15.1")

    //implementation(libs.retrofit)

    // Retrofit Moshi converter
    //implementation(libs.converter.moshi)

    // Optional: OkHttp for advanced networking and logging
    //implementation(libs.okhttp)
    //implementation(libs.logging.interceptor)

    //kapt(libs.androidx.room.compiler)
    //implementation(project(":UI_Lib"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
