package com.dancesurf.plugin

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.dancesurf.plugin.targetconfig.androidAppConfig
import com.dancesurf.plugin.utils.androidApplicationPlugin
import com.dancesurf.plugin.utils.apply
import com.dancesurf.plugin.utils.composeCompilerPlugin
import com.dancesurf.plugin.utils.kotlinAndroidPlugin
import com.dancesurf.plugin.utils.versionCatalog
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/**
 * Set up common settings to Android gradle modules.
 *
 * !!!***  Must be applied to Android Application module only ***!!!
 */
class AndroidAppGradlePlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(
                versionCatalog.androidApplicationPlugin.pluginId,
                versionCatalog.kotlinAndroidPlugin.pluginId,
                versionCatalog.composeCompilerPlugin.pluginId
            )

            extensions.configure<BaseAppModuleExtension>(androidAppConfig)
        }
    }
}