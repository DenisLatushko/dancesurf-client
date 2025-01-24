plugins {
    alias(libs.plugins.build.gradle.kmp)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.activity.ktx)
            implementation(libs.androidx.activity.compose)
            implementation(libs.android.compose.permissions)
        }
        commonMain.dependencies {
            implementation(projects.core.utils)
            implementation(compose.ui)
            implementation(compose.runtime)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
