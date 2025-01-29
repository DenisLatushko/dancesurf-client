package com.dancesurf.system.location.utils

import android.location.LocationManager
import android.location.LocationManager.GPS_PROVIDER
import android.location.LocationManager.NETWORK_PROVIDER
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.R
import com.dancesurf.system.location.LocationError
import com.dancesurf.system.permissions.Permission.ImpreciseLocation
import com.dancesurf.system.permissions.Permission.PreciseLocation
import com.dancesurf.system.permissions.PermissionChecker
import com.dancesurf.system.permissions.fetchPermissionStatus

actual class LocationServiceHealthKeeper(
    private val permissionChecker: PermissionChecker,
    private val locationManager: LocationManager
) {

    actual suspend fun checkHealth(): LocationError? = when {
        !isLocationEnabled() -> LocationError.ServiceNotEnabled
        !hasLocationPermission() -> LocationError.PermissionDenied
        else -> null
    }

    actual suspend fun hasLocationPermission(): Boolean {
        val hasCoarseLocationPermission = permissionChecker.fetchPermissionStatus(ImpreciseLocation)
        val hasFineLocationPermission = permissionChecker.fetchPermissionStatus(PreciseLocation)
        return hasCoarseLocationPermission || hasFineLocationPermission
    }

    private fun isLocationEnabled(): Boolean = locationManager.let {
        if (SDK_INT >= R) {
            it.isLocationEnabled
        } else {
            it.isProviderEnabled(GPS_PROVIDER) || it.isProviderEnabled(NETWORK_PROVIDER)
        }
    }
}