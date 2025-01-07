package com.dancesurf.plugin

import com.android.build.api.dsl.LibraryExtension
import com.dancesurf.plugin.targetconfig.kmpConfig
import com.dancesurf.plugin.targetconfig.kotlinAndroidConfig
import com.dancesurf.plugin.utils.androidLibraryPlugin
import com.dancesurf.plugin.utils.apply
import com.dancesurf.plugin.utils.kotlinMultiplatformPlugin
import com.dancesurf.plugin.utils.versionCatalog
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Set up common settings to Kotlin Multiplatform gradle modules
 *
 * !!!***  Must be applied to KMP modules only ***!!!
 */
class KMPBuildGradlePlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(
                versionCatalog.kotlinMultiplatformPlugin.pluginId,
                versionCatalog.androidLibraryPlugin.pluginId
            )

            extensions.configure<KotlinMultiplatformExtension>(kmpConfig)
            extensions.configure<LibraryExtension>(kotlinAndroidConfig)
        }
    }
}