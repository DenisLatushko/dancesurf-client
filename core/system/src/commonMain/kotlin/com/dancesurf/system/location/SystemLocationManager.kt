package com.dancesurf.system.location

import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.suspendCoroutine

/**
 * A manager to provide a suer location info
 */
expect class SystemLocationManager {

    /**
     * Fetch last known [LocationCoords]
     *
     * @param onFetched a callback to notify about the result. It designed with a callback because
     * systems can not return location immediately after the request.
     */
    fun fetchLastKnownLocation(onFetched: (Result<LocationCoords>) -> Unit)

    /**
     * Allow to subscribe to location updates
     */
    fun fetchUserLocation(): Flow<LocationCoords>

    /**
     * Stop all the services
     */
    fun terminate()
}

suspend fun SystemLocationManager.fetchLastKnownLocation(): LocationCoords =
    suspendCoroutine { continuation ->
        fetchLastKnownLocation { result ->
            continuation.resumeWith(result)
        }
    }