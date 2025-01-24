plugins {
    alias(libs.plugins.build.gradle.kmp)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    cocoapods {
        pod("GoogleMaps") {
            version = libs.versions.pods.google.maps.utils.get()
            extraOpts += listOf("-compiler-option", "-fmodules")
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.system)
            implementation(projects.core.utils)
            implementation(compose.ui)
            implementation(compose.material3)
            implementation(compose.foundation)
            implementation(compose.runtime)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        androidMain.dependencies {
            implementation(libs.android.compose.maps)
        }
    }
}