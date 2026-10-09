try {
    val processEnvClass = Class.forName("java.lang.ProcessEnvironment")
    val envField = processEnvClass.getDeclaredField("theEnvironment").apply { isAccessible = true }
    (envField.get(null) as? MutableMap<*, *>)?.remove("ANDROID_PREFS_ROOT")
    val caseInsensitiveEnvField = processEnvClass.getDeclaredField("theCaseInsensitiveEnvironment").apply { isAccessible = true }
    (caseInsensitiveEnvField.get(null) as? MutableMap<*, *>)?.remove("ANDROID_PREFS_ROOT")
} catch (_: Exception) {
}

pluginManagement {
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

rootProject.name = "DigimonExplorer"
include(":app")
