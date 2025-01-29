package com.dancesurf.system.location

import android.location.Location
import android.os.Looper
import com.dancesurf.system.location.utils.LocationServiceHealthKeeper
import com.dancesurf.system.location.utils.runIf
import com.dancesurf.system.location.utils.toLocationCoords
import com.dancesurf.system.location.utils.toResult
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val LOCATION_REQUEST_INTERVAL_MILLIS = 2000L
private const val LOCATION_REQUEST_MIN_DISTANCE_METERS = 50.0f

actual class SystemLocationManager(
    private val locationProvider: FusedLocationProviderClient,
    private val healthKeeper: LocationServiceHealthKeeper,
) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private val lastKnownUserLocationFlow = MutableStateFlow<LocationCoords?>(null)

    private val locationRequest: LocationRequest
        get() = LocationRequest.Builder(PRIORITY_HIGH_ACCURACY, LOCATION_REQUEST_INTERVAL_MILLIS)
            .setMinUpdateDistanceMeters(LOCATION_REQUEST_MIN_DISTANCE_METERS)
            .build()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            updatedLastKnownLocation(locationResult.lastLocation?.toLocationCoords())
        }
    }

    actual fun fetchLastKnownLocation(onFetched: (Result<LocationCoords>) -> Unit) {
        startUpdatingLocation()
        coroutineScope.launch {
            healthKeeper.runIf(
                healthy = { getLatUserLocationResult(onFetched) },
                dead = { onFetched(Result.failure(it)) }
            )
        }
    }

    private fun getLatUserLocationResult(onFetched: (Result<LocationCoords>) -> Unit) {
        lastKnownUserLocationFlow.value?.run {
            onFetched(Result.success(this))
        } ?: run {
            requestLastLocation { location ->
                updatedLastKnownLocation(location)
                onFetched(location.toResult())
            }
        }
    }

    private fun updatedLastKnownLocation(location: LocationCoords?) {
        if (location != null) {
            lastKnownUserLocationFlow.update { location }
        }
    }

    @Suppress("MissingPermission")
    private fun requestLastLocation(onFetched: (LocationCoords?) -> Unit) {
        locationProvider.lastLocation.addOnSuccessListener { location: Location? ->
            onFetched(location?.toLocationCoords())
        }
    }

    actual fun fetchUserLocation(): Flow<LocationCoords> {
        startUpdatingLocation()
        return lastKnownUserLocationFlow.filterNotNull()
    }

    @Suppress("MissingPermission")
    private fun startUpdatingLocation() {
        coroutineScope.launch {
            healthKeeper.runIf(
                healthy = {
                    locationProvider.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        Looper.getMainLooper()
                    )
                }
            )
        }
    }

    actual fun terminate() {
        locationProvider.removeLocationUpdates(locationCallback)
        coroutineScope.cancel()
    }
}