@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

// make libs.versions available
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        // path is from build-logic/ to the root gradle/ folder
        create("libs") { from(files("../gradle/libs.versions.toml")) }
    }
}
