package com.dancesurf.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * Initialize DI graph
 */
actual class DIInitializer(
    private val appContext: Context,
    private val isDebug: Boolean
) {
    actual fun start() {
        startKoin {
            androidContext(appContext)
            modules(sharedAppModule(isDebug))
            if (isDebug) androidLogger()
        }
    }
}