enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-plugin")
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "DanceSurf"
include(":androidApp")
include(":shared")
include(":core")
include(":core:network")
include(":core:utils")
include(":core:ui")
include(":core:system")
