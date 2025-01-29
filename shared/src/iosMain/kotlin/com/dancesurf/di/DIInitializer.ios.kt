package com.dancesurf.di

import org.koin.core.context.startKoin

/**
 * Initialize DI graph
 */
actual class DIInitializer(
    private val isDebug: Boolean
) {
    actual fun start() {
        startKoin {
            modules(sharedAppModule(isDebug))
        }
    }
}