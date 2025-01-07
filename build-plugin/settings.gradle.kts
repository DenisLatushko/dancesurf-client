dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }

    versionCatalogs.register("libs") {
        from(files("../gradle/libs.versions.toml"))
    }
}

rootProject.name = "build-plugin"
