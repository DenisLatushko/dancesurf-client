package com.dancesurf.plugin.utils

import org.gradle.api.plugins.PluginManager

/**
 * Apply plugins to the gradle module
 */
internal fun PluginManager.apply(vararg plugins: String) {
    plugins.forEach {
        apply(it)
    }
}