package com.dancesurf.plugin.targetconfig

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.dancesurf.plugin.AndroidConfig.COMPILE_SDK
import com.dancesurf.plugin.AndroidConfig.MIN_SDK
import com.dancesurf.plugin.JavaConfig.javaVersion
import com.dancesurf.plugin.JavaConfig.jvmTarget
import com.dancesurf.plugin.utils.nameSpace
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

/**
 * Set up common settings to Android gradle modules
 */
internal val Project.androidAppConfig: (BaseAppModuleExtension) -> Unit
    get() = { ext ->
        ext.apply {
            namespace = this@androidAppConfig.nameSpace
            compileSdk = COMPILE_SDK
            defaultConfig {
                minSdk = MIN_SDK
                targetSdk = COMPILE_SDK
            }

            buildFeatures {
                buildConfig = true
                compose = true
            }

            packaging {
                resources {
                    excludes += "/META-INF/{AL2.0,LGPL2.1}"
                }
            }

            buildTypes {
                debug {
                    isMinifyEnabled = false
                    isDebuggable = true
                    isJniDebuggable = true
                    isDefault = true
                }

                release {
                    isMinifyEnabled = true
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                }
            }

            compileOptions {
                sourceCompatibility = javaVersion
                targetCompatibility = javaVersion
            }

            kotlinExtension.jvmToolchain(jvmTarget.target.toInt())
        }
    }