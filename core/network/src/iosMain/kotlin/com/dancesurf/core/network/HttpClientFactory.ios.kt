package com.dancesurf.core.network

import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.darwin.Darwin

internal actual val httpClientEngine: HttpClientEngineFactory<HttpClientEngineConfig> = Darwin