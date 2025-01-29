package com.dancesurf.system.location.utils

import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.darwin.NSObject

internal class LocationManagerDelegate(
    private val onLocationUpdate: (CLLocation) -> Unit
): NSObject(), CLLocationManagerDelegateProtocol {

    override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
        val location = didUpdateLocations.lastOrNull() as? CLLocation
        location?.run { onLocationUpdate(this) }
    }
}