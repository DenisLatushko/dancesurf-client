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
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.kotlinx.coroutines.android)
    debugImplementation(libs.androidx.compose.ui.tooling)
}