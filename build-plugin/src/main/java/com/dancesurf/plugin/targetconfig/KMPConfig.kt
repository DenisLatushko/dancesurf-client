package com.dancesurf.plugin.targetconfig

import com.dancesurf.plugin.JavaConfig
import com.dancesurf.plugin.utils.moduleName
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.cocoapods.CocoapodsExtension

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

            sourceSets.all {
                languageSettings.enableLanguageFeature("ExpectActualClasses")
            }

            listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach { platform ->
                platform.binaries.framework {
                    baseName = moduleName
                    isStatic = true
                }

                println(">>> Base name for platform \"${platform.name}\" is $moduleName")
            }

            applyDefaultHierarchyTemplate()

            (this as? ExtensionAware)?.extensions
                ?.configure<CocoapodsExtension>(cocoapodsConfig)
                ?: run { println(">>> Cocoapods config not applied") }
        }
    }