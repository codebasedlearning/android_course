import kotlin.io.path.*

includeBuild("build-logic")

pluginManagement {
    //includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Android-Course"

fun includeUnits(vararg unitRoots: String) {
    unitRoots.forEach { root ->
        val rootPath = file(root).toPath()
        if (!rootPath.exists() || !rootPath.isDirectory()) return@forEach

        // one level deep: UnitX/<Module> with build.gradle(.kts)
        rootPath.toFile().listFiles { f -> f.isDirectory }?.forEach { modDir ->
            val hasBuild = modDir.toPath().resolve("build.gradle.kts").exists() ||
                           modDir.toPath().resolve("build.gradle").exists()
            if (hasBuild) {
                val gradlePath = ":${rootPath.fileName}:${modDir.name}"
                include(gradlePath)
                project(gradlePath).projectDir = modDir
            }
        }
    }
}

// all units with modules
includeUnits("Unit0x00", "Unit0x01", "Unit0x02", "Unit0x03")

/*

for simple modules we usually have
    include(":A_FancyApp", ":B_Widgets", ":C_Db")

in a project with folders we can use
    include(":A_FancyApp", ":B_Widgets", ":C_Db")
    project(":A_FancyApp").projectDir = file("Unit0x00/A_FancyApp")
    ...

this is basically what includeUnits does, it collects modules in folders
*/
