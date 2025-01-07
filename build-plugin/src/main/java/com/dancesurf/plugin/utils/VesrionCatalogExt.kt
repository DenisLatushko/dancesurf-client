package com.dancesurf.plugin.utils

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.plugin.use.PluginDependency

internal val Project.versionCatalog: VersionCatalog
    get() = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

internal val VersionCatalog.kotlinMultiplatformPlugin: PluginDependency
    get() = findPlugin("kotlin.multiplatform").get().get()

internal val VersionCatalog.androidLibraryPlugin: PluginDependency
    get() = findPlugin("android.library").get().get()