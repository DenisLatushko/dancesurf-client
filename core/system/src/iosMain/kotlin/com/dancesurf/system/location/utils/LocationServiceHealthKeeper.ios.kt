package com.dancesurf.system.location.utils

import com.dancesurf.system.location.LocationError
import com.dancesurf.system.permissions.Permission.ImpreciseLocation
import com.dancesurf.system.permissions.Permission.PreciseLocation
import com.dancesurf.system.permissions.PermissionChecker
import com.dancesurf.system.permissions.PermissionStatus.Denied
import com.dancesurf.system.permissions.delegate.LocationPermissionDelegate
import com.dancesurf.system.permissions.fetchPermissionStatus
import platform.CoreLocation.CLLocationManager

actual class LocationServiceHealthKeeper(
    private val locationManager: CLLocationManager,
    private val permissionChecker: PermissionChecker
) {
    actual suspend fun checkHealth(): LocationError? = when {
        !hasLocationPermission() -> LocationError.PermissionDenied
        !locationManager.locationServicesEnabled -> LocationError.ServiceNotEnabled
        else -> null
    }

    actual suspend fun hasLocationPermission(): Boolean {
        val hasCoarseLocationPermission = permissionChecker.fetchPermissionStatus(ImpreciseLocation)
        val hasFineLocationPermission = permissionChecker.fetchPermissionStatus(PreciseLocation)
        return hasCoarseLocationPermission || hasFineLocationPermission
    }
}