pluginManagement {
    repositories {
        google()                // ✅ Required for AndroidX + Material
        mavenCentral()          // ✅ General libraries
        gradlePluginPortal()    // ✅ Gradle plugins
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()                // ✅ Required for AndroidX + Material
        mavenCentral()          // ✅ General libraries
    }
}

rootProject.name = "add_friends"
include(":app")