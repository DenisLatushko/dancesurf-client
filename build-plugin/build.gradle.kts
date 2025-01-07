plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

dependencies {
    compileOnly(libs.android.gradle)
    compileOnly(libs.kotlin.gradle)
    compileOnly(libs.compose.gradle)
}

gradlePlugin {
    plugins.register("kmp-build-gradle-plugin") {
        id = "kmp-build-gradle-plugin"
        version = "1.0.0"
        group = "com.dancesurf.plugin"
        implementationClass = "com.dancesurf.plugin.KMPBuildGradlePlugin"
    }

    plugins.register("android-app-build-gradle-plugin") {
        id = "android-app-build-gradle-plugin"
        version = "1.0.0"
        group = "com.dancesurf.plugin"
        implementationClass = "com.dancesurf.plugin.AndroidAppGradlePlugin"
    }
}