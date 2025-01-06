package com.dancesurf.logger

import com.dancesurf.utils.BuildConfig
import org.koin.core.component.KoinComponent
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter

/**
 * Convert date to string with formatter
 */
private fun NSDate.format(
    formatter: NSDateFormatter
): String = formatter.stringFromDate(this)

actual object Log : KoinComponent {

    private val canLog: Boolean by lazy {
        getKoin().get<BuildConfig>().isDebug
    }

    private val dateFormatter by lazy {
        NSDateFormatter().apply {
            dateFormat = "MM-dd HH:mm:ss.SSS"
        }
    }

    actual fun d(tag: String, message: String) {
        log(tag, message, LogLevel.DEBUG)
    }

    actual fun e(tag: String, message: String, throwable: Throwable?) {
        log(tag, message, LogLevel.ERROR)
    }

    actual fun i(tag: String, message: String) {
        log(tag, message, LogLevel.INFO)
    }

    actual fun w(tag: String, message: String) {
        log(tag, message, LogLevel.WARNING)
    }

    private fun log(tag: String, message: String, level: LogLevel) {
        if (canLog) {
            println(buildMessage(tag, message, level))
        }
    }

    private fun buildMessage(tag: String, message: String, level: LogLevel): String =
        "[${NSDate().format(dateFormatter)}] - ${level.value} - $tag: $message"
}


private enum class LogLevel(
    val value: String
) {
    DEBUG("⚫ DEBUG"),
    INFO("\uD83D\uDFE2 INFO"),
    ERROR("\uD83D\uDD34 ERROR"),
    WARNING("\uD83D\uDFE3 WARNING")
}