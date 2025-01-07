plugins {
    alias(libs.plugins.build.gradle.androidApp)
}

android {
    defaultConfig {
        applicationId = "com.dancesurf.android"
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    implementation(projects.shared)
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.kotlin.coroutines.android)
    debugImplementation(libs.compose.ui.tooling)
}