package com.dancesurf.logger

import android.util.Log
import com.dancesurf.utils.BuildConfig
import org.koin.core.component.KoinComponent

actual object Log : KoinComponent {

    private val canLog: Boolean by lazy {
        getKoin().get<BuildConfig>().isDebug
    }

    actual fun d(tag: String, message: String) {
        if (canLog) {
            Log.d(tag, message)
        }
    }

    actual fun e(tag: String, message: String, throwable: Throwable?) {
        if (canLog) {
            Log.e(tag, message, throwable)
        }
    }

    actual fun i(tag: String, message: String) {
        if (canLog) {
            Log.i(tag, message)
        }
    }

    actual fun w(tag: String, message: String) {
        if (canLog) {
            Log.w(tag, message)
        }
    }
}