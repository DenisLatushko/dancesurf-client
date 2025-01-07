package com.dancesurf.plugin.utils

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.plugin.use.PluginDependency

internal val Project.versionCatalog: VersionCatalog
    get() = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

internal val VersionCatalog.androidApplicationPlugin: PluginDependency
    get() = findPlugin("android.application").get().get()

internal val VersionCatalog.kotlinAndroidPlugin: PluginDependency
    get() = findPlugin("kotlin.android").get().get()

internal val VersionCatalog.composeCompilerPlugin: PluginDependency
    get() = findPlugin("compose.compiler").get().get()

internal val VersionCatalog.kotlinMultiplatformPlugin: PluginDependency
    get() = findPlugin("kotlin.multiplatform").get().get()

internal val VersionCatalog.androidLibraryPlugin: PluginDependency
    get() = findPlugin("android.library").get().get()