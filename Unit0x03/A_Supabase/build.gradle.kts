import java.util.Properties
import kotlin.apply

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("android-conventions")

    alias(libs.plugins.kotlin.kapt)
    kotlin("plugin.serialization") version "2.0.21"
}

android {
    val properties = Properties().apply {
        load(project.rootProject.file("local.properties").inputStream())
    }
    val supabase_url: String = properties.getProperty("supabase_url") ?: ""
    val supabase_anon_key: String = properties.getProperty("supabase_anon_key") ?: ""
    val supabase_user_email: String = properties.getProperty("supabase_user_email") ?: ""
    val supabase_user_password: String = properties.getProperty("supabase_user_password") ?: ""

    namespace = "de.fh_aachen.android.supabase"
    // compileSdk = 35

    defaultConfig {
        applicationId = "de.fh_aachen.android.supabase"
        // minSdk = 27
        // targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "supabase_url", "\"$supabase_url\"")
        buildConfigField("String", "supabase_anon_key", "\"$supabase_anon_key\"")
        buildConfigField("String", "supabase_user_email", "\"$supabase_user_email\"")
        buildConfigField("String", "supabase_user_password", "\"$supabase_user_password\"")

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

    kotlin {
        jvmToolchain(21)
    }

    buildFeatures {
        compose = true
        buildConfig = true
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
    //implementation(libs.material)
    implementation(libs.androidx.material3)
    implementation("androidx.compose.material:material-icons-extended:1.6.7")
    //implementation("androidx.compose.material:material-icons-extended:1.13.0")
    //implementation("androidx.compose.material:material:1.9.3")
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.activity.compose)
    //implementation("androidx.compose.material3:material3-icons-extended:1.13.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(platform("io.github.jan-tennert.supabase:bom:3.2.5"))
    implementation("io.github.jan-tennert.supabase:postgrest-kt")
    implementation("io.github.jan-tennert.supabase:realtime-kt")
    //implementation("io.github.jan-tennert.supabase:gotrue-kt")
    //implementation("io.ktor:ktor-client-android:3.3.1")
    implementation("io.ktor:ktor-client-okhttp:3.3.1")
    implementation("io.ktor:ktor-client-websockets:3.3.1")

}
