package com.dancesurf.android

import android.app.Application
import com.dancesurf.androidApp.BuildConfig
import com.dancesurf.di.DIInitializer

class MainApp: Application() {

    override fun onCreate() {
        super.onCreate()
        DIInitializer(
            isDebug = BuildConfig.DEBUG,
            appContext = this
        ).start()
    }
}