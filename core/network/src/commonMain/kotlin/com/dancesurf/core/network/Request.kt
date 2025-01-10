package com.dancesurf.core.network

import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.util.reflect.TypeInfo
import kotlinx.serialization.Serializable

/**
 * A HTTP request types
 *
 * @param T is a type of response to be returned
 */
sealed class Request<T> {
    abstract val protocol: URLProtocol
    abstract val path: String
    abstract val headers: Map<String, String>
    abstract val parameters: Map<String, String>

    class Get<E>(
        override val protocol: URLProtocol = URLProtocol.HTTPS,
        override val path: String,
        override val headers: Map<String, String> = emptyMap(),
        override val parameters: Map<String, String> = emptyMap(),
    ): Request<E>()

    class Post<E, R>(
        override val protocol: URLProtocol = URLProtocol.HTTPS,
        override val path: String,
        override val headers: Map<String, String> = emptyMap(),
        override val parameters: Map<String, String> = emptyMap(),
        val body: Body<R>?
    ): Request<E>()
}

class Body<T>(
    val type: TypeInfo,
    val body: T
)
