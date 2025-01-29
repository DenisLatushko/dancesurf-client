package com.dancesurf.system.location

/**
 * An error list to provide a reason why location service can not be used right now
 */
sealed class LocationError: Throwable() {
    data object ServiceNotEnabled: LocationError()
    data object PermissionDenied: LocationError()
    data object LocationNotAvailable: LocationError()
}