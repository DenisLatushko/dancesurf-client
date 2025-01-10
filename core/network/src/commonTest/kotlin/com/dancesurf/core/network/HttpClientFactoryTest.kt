package com.dancesurf.core.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.config
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpHeaders.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.HttpStatusCode.Companion.allStatusCodes
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private const val JSON_RESPONSE_SUCCESS = """{"ip":"127.0.0.1"}"""
private const val BASE_URL = "http://127.0.0.1"
private const val URL_PATH = "/test"

class HttpClientFactoryTest {

    private val testResponseData by lazy { TestResponseData(ip = "127.0.0.1") }
    private val responseHeaders by lazy { mapOf(ContentType to "application/json") }

    @Test
    fun `given success get response when call client then data returned`() = runTest {
        assertSuccessResponse(
            request = { client ->
                client.safeGet<TestResponseData>(
                    request = Request.Get(
                        headers = responseHeaders,
                        path = URL_PATH
                    )
                )
            }
        )
    }

    @Test
    fun `given success post response when call client then data returned`() = runTest {
        assertSuccessResponse(
            request = { client ->
                client.safePost<TestResponseData, Any?>(
                    request = Request.Post(
                        headers = responseHeaders,
                        path = URL_PATH
                    )
                )
            }
        )
    }

    private suspend fun assertSuccessResponse(
        request: suspend (HttpClient) -> Result<TestResponseData>
    ) {
        val headers = headersOf(HttpHeaders.ContentType, "application/json")

        _2xxStatusCodeSuccessList.forEach { code ->
            val client = getHttpClient(
                responseData = JSON_RESPONSE_SUCCESS,
                status = code,
                headers = headers
            )

            val response = request(client)

            assertTrue(response.isSuccess)
            assertEquals(testResponseData, response.getOrThrow())
        }
    }

    @Test
    fun `given error get response 3xx when call client then error returned`() = runTest {
        assertGetErrorResponse(
            statusList = _3xxStatusCodeSuccessList,
            exceptionType = RedirectResponseException::class
        )
    }

    @Test
    fun `given error post response 3xx when call client then error returned`() = runTest {
        assertPostErrorResponse(
            statusList = _3xxStatusCodeSuccessList,
            exceptionType = RedirectResponseException::class
        )
    }

    @Test
    fun `given error get response 4xx when call client then error returned`() = runTest {
        assertGetErrorResponse(
            statusList = _4xxStatusCodeSuccessList,
            exceptionType = ClientRequestException::class
        )
    }

    @Test
    fun `given error post response 4xx when call client then error returned`() = runTest {
        assertPostErrorResponse(
            statusList = _4xxStatusCodeSuccessList,
            exceptionType = ClientRequestException::class
        )
    }

    @Test
    fun `given error get response 5xx when call client then error returned`() = runTest {
        assertGetErrorResponse(
            statusList = _5xxStatusCodeSuccessList,
            exceptionType = ServerResponseException::class
        )
    }

    @Test
    fun `given error post response 5xx when call client then error returned`() = runTest {
        assertPostErrorResponse(
            statusList = _5xxStatusCodeSuccessList,
            exceptionType = ServerResponseException::class
        )
    }

    private suspend fun <T: ResponseException> assertGetErrorResponse(
        statusList: List<HttpStatusCode>,
        exceptionType: KClass<T>
    ) {
        assertErrorResponse(
            statusList = statusList,
            exceptionType = exceptionType,
            headers = Headers.Empty,
            request = { client ->
                client.safeGet<TestResponseData>(
                    request = Request.Get(
                        headers = responseHeaders,
                        path = URL_PATH
                    )
                )
            }
        )
    }

    private suspend fun <T: ResponseException> assertPostErrorResponse(
        statusList: List<HttpStatusCode>,
        exceptionType: KClass<T>
    ) {
        assertErrorResponse(
            statusList = statusList,
            exceptionType = exceptionType,
            headers = Headers.Empty,
            request = { client ->
                client.safePost<TestResponseData, Any?>(
                    request = Request.Post(
                        headers = responseHeaders,
                        path = URL_PATH
                    )
                )
            }
        )
    }

    private suspend fun <T: ResponseException> assertErrorResponse(
        statusList: List<HttpStatusCode>,
        exceptionType: KClass<T>,
        request: suspend (HttpClient) -> Result<TestResponseData>,
        headers: Headers = headersOf(
            Pair(HttpHeaders.ContentType, listOf("text/html")),
            Pair(HttpHeaders.Location, listOf(""))
        )
    ) {

        statusList.forEach { code ->
            val client = getHttpClient(
                responseData = code.description,
                status = code,
                headers = headers
            )

            val response = request(client)

            assertTrue(response.isFailure)
            assertTrue(response.exceptionOrNull() is ErrorType.HttpError)
            assertEquals(code.value, (response.exceptionOrNull() as ErrorType.HttpError).code)
            assertEquals(exceptionType, (response.exceptionOrNull() as ErrorType.HttpError).cause!!::class)
        }
    }

    private fun getHttpClient(
        responseData: String,
        status: HttpStatusCode,
        headers: Headers
    ) = newHttpClient(
        HttpClientParams.default(
            baseUrl = BASE_URL,
            httpClientEngine = MockEngine.config {
                addHandler {
                    respond(
                        content = responseData,
                        status = status,
                        headers = headers
                    )
                }

            }
        )
    ).config {
        followRedirects = false
    }

    private companion object {
        val _2xxStatusCodeSuccessList = allStatusCodes.filter { it.value / 100 == 2 }
        val _3xxStatusCodeSuccessList = allStatusCodes.filter { it.value / 100 == 3 }
        val _4xxStatusCodeSuccessList = allStatusCodes.filter { it.value / 100 == 4 }
        val _5xxStatusCodeSuccessList = allStatusCodes.filter { it.value / 100 == 5 }
    }
}

@Serializable
private data class TestResponseData(
    val ip: String = ""
)