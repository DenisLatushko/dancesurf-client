package com.dancesurf.di

import com.dancesurf.system.di.systemMainModule
import com.dancesurf.utils.di.utilsMainModule
import org.koin.core.module.Module

/**
 * Provide a list of modules for the whole "shared" gradle module
 */
internal fun sharedAppModule(isDebug: Boolean): List<Module> =
    mutableListOf(utilsMainModule(isDebug)).apply {
        add(utilsMainModule(isDebug))
        addAll(systemMainModule())
    }