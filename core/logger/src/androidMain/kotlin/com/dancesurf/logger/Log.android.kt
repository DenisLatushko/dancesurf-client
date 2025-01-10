package com.dancesurf.logger

typealias AndroidLog = android.util.Log

actual class Log(private val isLoggable: Boolean) {

    actual fun d(tag: String, message: String) {
        if (isLoggable) {
            AndroidLog.d(tag, message)
        }
    }

    actual fun e(tag: String, message: String, throwable: Throwable?) {
        if (isLoggable) {
            AndroidLog.e(tag, message, throwable)
        }
    }

    actual fun i(tag: String, message: String) {
        if (isLoggable) {
            AndroidLog.i(tag, message)
        }
    }

    actual fun w(tag: String, message: String) {
        if (isLoggable) {
            AndroidLog.w(tag, message)
        }
    }

    actual companion object {
        actual fun get(isLoggable: Boolean): Log  = Log(isLoggable)
    }
}