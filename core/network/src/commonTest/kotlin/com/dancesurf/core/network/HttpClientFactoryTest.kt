package com.dancesurf.core.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.config
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondBadRequest
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ResponseException
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.HttpStatusCode.Companion.allStatusCodes
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.charsets.Charsets
import io.ktor.utils.io.core.toByteArray
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private const val JSON_RESPONSE_SUCCESS = """{"ip":"127.0.0.1"}"""
private const val JSON_RESPONSE_ERROR = ""
private const val BASE_URL = "http://127.0.0.1"
private const val URL_PATH = "/test"

class HttpClientFactoryTest {

    private val testResponseData by lazy { TestResponseData(ip = "127.0.0.1") }
    private val responseHeaders by lazy {
        mapOf(HttpHeaders.ContentType to "application/json")
    }

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
                        path = URL_PATH,
                        body = null
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
        assertErrorResponse(
            statusList = _3xxStatusCodeSuccessList,
            exceptionType = RedirectResponseException::class,
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

    private suspend fun <T: ResponseException> assertErrorResponse(
        statusList: List<HttpStatusCode>,
        exceptionType: KClass<T>,
        request: suspend (HttpClient) -> Result<TestResponseData>,
        headers: Headers = headersOf("Content-Type", "text/htm")
    ) {

        statusList.forEach { code ->
            val client = getHttpClient(
                responseData = code.description,
                status = code,
                headers = headers
            )

            val response = request(client)

            assertTrue(response.isFailure)

            //TODO Uncomment assertions below and fix the issue
//            assertTrue(response.exceptionOrNull() is ErrorType.HttpError)
//            assertEquals(code.value, (response.exceptionOrNull() as ErrorType.HttpError).code)
//            assertEquals(exceptionType, (response.exceptionOrNull() as ErrorType.HttpError).cause!!::class)
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
                        content = ByteReadChannel("Bad request".toByteArray(Charsets.UTF_8)),
                        status = HttpStatusCode.BadRequest,
                        headers = headersOf("Content-Type", "text/htm")
                    )
                }
            }
        )
    )

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