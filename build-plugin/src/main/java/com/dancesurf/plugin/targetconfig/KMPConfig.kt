package com.dancesurf.plugin.targetconfig

import com.dancesurf.plugin.JavaConfig
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Set up common settings to Kotlin Multiplatform gradle modules
 */
internal val Project.kmpConfig: (KotlinMultiplatformExtension) -> Unit
    get() = { ext ->
        ext.apply {
            androidTarget {
                compilations.all {
                    compileTaskProvider.configure {
                        compilerOptions {
                            jvmTarget.set(JavaConfig.jvmTarget)
                        }
                    }
                }
            }

            listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach { platform ->
                val fullBaseName = path.drop(1).replace(":", "_")
                println(">>> Base name for \"$name\" is $fullBaseName")

                platform.binaries.framework {
                    baseName = fullBaseName
                    isStatic = true
                }
            }

            applyDefaultHierarchyTemplate()
        }
    }