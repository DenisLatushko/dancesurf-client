package com.dancesurf.system.location.utils

import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority.PRIORITY_BALANCED_POWER_ACCURACY
import com.google.android.gms.location.SettingsClient
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

private const val LOCATION_REQUEST_INTERVAL_MILLIS = 2000L

internal class LocationServiceSettingsProvider(
    private val locationSettingsClient: SettingsClient
) {

    suspend fun requestSettings(
        priority: Int = PRIORITY_BALANCED_POWER_ACCURACY,
        intervalMillis: Long = LOCATION_REQUEST_INTERVAL_MILLIS
    ): Result<LocationSettingsResponse> = runCatching {
        val locationRequest = LocationRequest.Builder(priority, intervalMillis)
            .build()

        val locationSettingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .build()

        locationSettingsClient
            .checkLocationSettings(locationSettingsRequest)
            .await()
    }.onFailure { ex ->
        if (ex is CancellationException) throw ex
    }
}