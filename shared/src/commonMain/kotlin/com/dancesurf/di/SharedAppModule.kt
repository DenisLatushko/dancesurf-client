package com.dancesurf.di

import com.dancesurf.utils.di.utilsMainModule
import org.koin.core.module.Module

/**
 * Provide a list of modules for the whole "shared" gradle module
 */
internal fun sharedAppModule(isDebug: Boolean): List<Module> = listOf(
    utilsMainModule(isDebug)
)