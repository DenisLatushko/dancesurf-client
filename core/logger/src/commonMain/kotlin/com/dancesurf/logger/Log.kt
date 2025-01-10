package com.dancesurf.logger

/**
 * A logger wrapper to print messages to console for debugging purposes.
 */
expect class Log {
    fun d(tag: String, message: String)
    fun e(tag: String, message: String, throwable: Throwable? = null)
    fun i(tag: String, message: String)
    fun w(tag: String, message: String)

    companion object {
        fun get(isLoggable: Boolean): Log
    }
}

