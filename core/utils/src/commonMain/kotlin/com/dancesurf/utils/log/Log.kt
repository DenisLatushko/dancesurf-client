package com.dancesurf.utils.log

/**
 * A logger wrapper to print messages to console for debugging purposes.
 */
expect class Log {
    companion object {
        var isLoggable: Boolean

        fun d(tag: String, message: String)
        fun e(tag: String, message: String, throwable: Throwable? = null)
        fun i(tag: String, message: String)
        fun w(tag: String, message: String)
    }
}

