package com.dancesurf.system.location

import com.dancesurf.system.location.utils.LocationManagerDelegate
import com.dancesurf.system.location.utils.LocationServiceHealthKeeper
import com.dancesurf.system.location.utils.runIf
import com.dancesurf.system.location.utils.toLocationCoords
import com.dancesurf.system.location.utils.toResult
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.kCLLocationAccuracyBest

private const val DEFAULT_DISTANCE_FILTER_METERS = 100.0

@OptIn(ExperimentalForeignApi::class)
actual class SystemLocationManager(
    private val locationManager: CLLocationManager,
    private val healthKeeper: LocationServiceHealthKeeper
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private val lastKnownUserLocationFlow = MutableStateFlow(
        locationManager.location?.coordinate?.toLocationCoords()
    )

    init {
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        locationManager.distanceFilter = DEFAULT_DISTANCE_FILTER_METERS
        locationManager.delegate = LocationManagerDelegate { location ->
            val userLocation = location.coordinate.toLocationCoords()
            lastKnownUserLocationFlow.update { userLocation }
        }
        startUpdatingLocation()
    }

    actual fun fetchLastKnownLocation(onFetched: (Result<LocationCoords>) -> Unit) {
        startUpdatingLocation()
        coroutineScope.launch {
            healthKeeper.runIf(
                healthy = {
                    val newLocation = locationManager.location?.coordinate?.toLocationCoords()
                    lastKnownUserLocationFlow.update { newLocation }
                    onFetched(newLocation.toResult())
                },
                dead = { onFetched(Result.failure(it)) }
            )
        }
    }

    actual fun fetchUserLocation(): Flow<LocationCoords> {
        startUpdatingLocation()
        return lastKnownUserLocationFlow.filterNotNull()
    }

    private fun startUpdatingLocation() {
        coroutineScope.launch {
            healthKeeper.runIf(
                healthy = { locationManager.startUpdatingLocation() }
            )
        }
    }

    /**
     * Stop all the services
     */
    actual fun terminate() {
        locationManager.stopUpdatingLocation()
    }
}
