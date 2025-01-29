package com.dancesurf.system.location.utils

import com.dancesurf.system.location.LocationError

expect class LocationServiceHealthKeeper {
    suspend fun checkHealth(): LocationError?
    suspend fun hasLocationPermission(): Boolean
}

internal suspend fun LocationServiceHealthKeeper.runIf(
    healthy: (() -> Unit)? = null,
    dead: ((LocationError) -> Unit)? = null
) {
    when(val error = checkHealth()) {
        null -> healthy?.invoke()
        else -> dead?.invoke(error)
    }
}