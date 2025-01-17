package com.dancesurf.plugin.targetconfig

import com.dancesurf.plugin.IosConfig
import com.dancesurf.plugin.utils.moduleName
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.cocoapods.CocoapodsExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

/**
 * Set up common settings to Kotlin Multiplatform gradle modules
 */
@OptIn(ExperimentalKotlinGradlePluginApi::class)
internal val Project.cocoapodsConfig: (CocoapodsExtension) -> Unit
    get() = { ext ->
        ext.apply {
            name = moduleName
            summary = "KMP Module"
            homepage = "Link to KMP Module"
            version = "2.0"
            ios.deploymentTarget = IosConfig.DEPLOYMENT_TARGET

            framework {
                baseName = moduleName
                isStatic = true
                transitiveExport = false
            }

            println(">>> Cocoapods name and base name in \"$name\" is $moduleName")

            xcodeConfigurationToNativeBuildType["CUSTOM_DEBUG"] = NativeBuildType.DEBUG
            xcodeConfigurationToNativeBuildType["CUSTOM_RELEASE"] = NativeBuildType.RELEASE
        }
    }