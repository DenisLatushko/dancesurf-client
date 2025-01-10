package com.dancesurf.core.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod.Companion.Get
import io.ktor.http.HttpMethod.Companion.Post
import io.ktor.http.contentType
import io.ktor.http.headers
import io.ktor.http.parameters
import io.ktor.http.path
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException


/**
 * Do a GET request and return [T] wrapped by [Result]
 *
 * @return [Result] which contains response body or error
 */
suspend inline fun <reified T> HttpClient.safeGet(
    request: Request.Get<T>
): Result<T> = safeRequest<T> {
    get {
        url {
            protocol = request.protocol
            path(request.path)
            headers(request.headers)
            parameters(request.parameters)
        }
    }
}

suspend inline fun <reified T, reified E> HttpClient.safePost(
    request: Request.Post<T, E>
): Result<T> = safeRequest<T> {
    post {
        url {
            protocol = request.protocol
            path(request.path)
            headers(request.headers)
            parameters(request.parameters)
        }

        request.body?.run {
            setBody(body, type)
        }
    }
}

fun headers(headers: Map<String, String>) {
    headers {
        headers.forEach { (key, value) ->
            append(key, value)
        }
    }
}

fun parameters(params: Map<String, String>) {
    parameters {
        params.forEach { (key, value) ->
            append(key, value)
        }
    }
}

/**
 * Catch any exception or response
 *
 * @param requestBlock a request block to be executed
 * @return [Result] wrapped by [T]
 */
suspend inline fun <reified T> HttpClient.safeRequest(
    requestBlock: HttpRequestBuilder.() -> Unit
): Result<T> = try {
    val response = request(requestBlock)
    Result.success(response.body())
} catch (e: ResponseException) {
    Result.failure(
        exception = ErrorType.HttpError(
            code = e.response.status.value,
            cause = e.cause
        )
    )
} catch (e: IOException) {
    Result.failure(
        exception = ErrorType.NetworkError(
            cause = e.cause
        )
    )
} catch (e: SerializationException) {
    Result.failure(
        exception = ErrorType.SerializationError(
            cause = e.cause
        )
    )
} catch (e: Exception) {
    Result.failure(
        exception = ErrorType.GenericError(
            cause = e.cause
        )
    )
}