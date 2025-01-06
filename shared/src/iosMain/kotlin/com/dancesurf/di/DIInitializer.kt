package com.dancesurf.di

import org.koin.core.context.startKoin

/**
 * Initialize DI graph
 */
class DIInitializer {
    companion object {
        fun initDi(isDebug: Boolean) {
            startKoin {
                modules(sharedAppModule(isDebug))
            }
        }
    }
}