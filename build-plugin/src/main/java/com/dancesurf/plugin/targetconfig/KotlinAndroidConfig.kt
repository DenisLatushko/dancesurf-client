package com.dancesurf.plugin.targetconfig

import com.android.build.api.dsl.LibraryExtension
import com.dancesurf.plugin.AndroidConfig.COMPILE_SDK
import com.dancesurf.plugin.AndroidConfig.MIN_SDK
import com.dancesurf.plugin.JavaConfig.javaVersion
import com.dancesurf.plugin.utils.nameSpace
import org.gradle.api.Project

internal val Project.kotlinAndroidConfig: (LibraryExtension) -> Unit
    get() = { ext ->
        ext.apply {
            namespace = this@kotlinAndroidConfig.nameSpace
            compileSdk = COMPILE_SDK
            defaultConfig {
                minSdk = MIN_SDK
            }
            compileOptions {
                sourceCompatibility = javaVersion
                targetCompatibility = javaVersion
            }
        }
    }

