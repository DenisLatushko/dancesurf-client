package com.dancesurf.core.network

import com.dancesurf.utils.log.Log
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.serialization.json.Json

private const val LOG_TAG = "HttpClient"
private const val DEFAULT_TIMEOUT_MS = 30000L

/**
 * An [HttpClient] provided by a platform
 */
internal expect val httpClientEngine: HttpClientEngineFactory<HttpClientEngineConfig>

/**
 * A factory function to initialize and set up [HttpClient] to be used in future API calls
 */
fun newHttpClient(clientParams: HttpClientParams): HttpClient =
    HttpClient(clientParams.httpClientEngine).config {
        expectSuccess = true

        engine()
        timeout(clientParams)
        clientParams.loggerParams?.run {
            logging(this)
        }
        defaultRequestWithParams(clientParams)
        contentNegotiation()
    }

private fun HttpClientConfig<*>.engine() {
    engine {
        dispatcher = Dispatchers.IO
    }
}

private fun HttpClientConfig<*>.defaultRequestWithParams(clientParams: HttpClientParams) {
    defaultRequest {
        url(clientParams.baseUrl)
        clientParams.headers.forEach { (key, value) ->
            header(key, value)
        }
    }
}

private fun HttpClientConfig<*>.logging(loggingParams: HttpClientLoggerParams) {
    install(Logging) {
        logger = loggingParams.logger
        level = loggingParams.level
        logger = object : Logger {

            private val logger = Log.get(true)

            override fun log(message: String) {
                logger.d(LOG_TAG, message)
            }
        }
    }
}

private fun HttpClientConfig<*>.timeout(clientParams: HttpClientParams) {
    install(HttpTimeout) {
        requestTimeoutMillis = clientParams.requestTimeout
        socketTimeoutMillis = clientParams.socketTimeout
    }
}

private fun HttpClientConfig<*>.contentNegotiation() {
    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
                isLenient = false
                ignoreUnknownKeys = true
            }
        )
    }
}

data class HttpClientParams(
    val httpClientEngine: HttpClientEngineFactory<HttpClientEngineConfig>,
    val baseUrl: String,
    val maxRetries: Int = 0,
    val requestTimeout: Long = DEFAULT_TIMEOUT_MS,
    val socketTimeout: Long = DEFAULT_TIMEOUT_MS,
    val headers: Map<String, String> = emptyMap(),
    val loggerParams: HttpClientLoggerParams? = null
) {
    companion object {
        fun default(
            baseUrl: String,
            httpClientEngine: HttpClientEngineFactory<HttpClientEngineConfig>
        ) = HttpClientParams(
            httpClientEngine = httpClientEngine,
            baseUrl = baseUrl
        )

        fun defaultDebug(
            baseUrl: String,
            httpClientEngine: HttpClientEngineFactory<HttpClientEngineConfig>
        ) = default(
            baseUrl = baseUrl,
            httpClientEngine = httpClientEngine
        ).copy(
            loggerParams = HttpClientLoggerParams(
                logger = Logger.DEFAULT,
                level = LogLevel.ALL
            )
        )
    }
}

class HttpClientLoggerParams(
    val logger: Logger,
    val level: LogLevel
)