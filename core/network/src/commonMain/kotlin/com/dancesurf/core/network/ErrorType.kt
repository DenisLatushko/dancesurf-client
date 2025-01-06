package com.dancesurf.core.network

/**
 * A basic error types produces by request/response API flow
 */
sealed class ErrorType(
    message: String?,
    cause: Throwable?
): Throwable(message = message, cause = cause) {

    /**
     * IO network errors
     *
     * @param cause a [Throwable] object which is a cause of an issue
     */
    class NetworkError(
        cause: Throwable? = null
    ): ErrorType(message = null, cause = cause)

    /**
     * A basic http error type cause by non 2** response status
     *
     * @property code http status code
     * @param cause a [Throwable] object which is a cause of an issue
     */
    class HttpError(
        val code: Int,
        message: String? = null,
        cause: Throwable? = null
    ): ErrorType(message = message, cause = cause)

    /**
     * An error produces by internal API flow
     *
     * @property code an internal error code
     * @param message an error code description
     */
    class ApiError(
        val code: Int,
        message: String? = null
    ): ErrorType(message = message, cause = null)

    /**
     * An error appeared during serialization/deserialization process
     *
     * @param message an error message
     * @param cause a [Throwable] object which is a cause of an issue
     */
    class SerializationError(
        message: String? = null,
        cause: Throwable? = null
    ): ErrorType(message = message, cause = cause)

    /**
     * Unknown issue
     *
     * @param message an error message
     * @param cause a [Throwable] object which is a cause of an issue
     */
    class GenericError(
        message: String? = null,
        cause: Throwable? = null
    ): ErrorType(message = message, cause = cause)
}