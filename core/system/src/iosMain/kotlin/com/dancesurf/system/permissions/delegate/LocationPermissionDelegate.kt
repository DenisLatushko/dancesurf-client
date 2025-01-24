package com.dancesurf.system.permissions.delegate

import com.dancesurf.system.permissions.PermissionStatus
import com.dancesurf.system.permissions.PermissionStatus.Denied
import com.dancesurf.system.permissions.PermissionStatus.Granted
import platform.CoreLocation.CLAuthorizationStatus
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined
import platform.darwin.NSObject


/**
 * A delegate for requesting location permission
 */
internal class LocationPermissionDelegate: PermissionDelegate {

    private val locationManager = CLLocationManager()
    override var onPermissionResult: ((PermissionStatus) -> Unit)? = null
    private var isSkipCallback = true

    override val currentPermissionStatus: PermissionStatus
        get() = CLLocationManager.authorizationStatus()
            .toPermissionStatusDetailed()

    init {
        locationManager.delegate = object: NSObject(), CLLocationManagerDelegateProtocol {
            override fun locationManager(
                manager: CLLocationManager,
                didChangeAuthorizationStatus: CLAuthorizationStatus
            ) {
                if (isSkipCallback) {
                    isSkipCallback = false
                    return
                }

                val status = didChangeAuthorizationStatus.toPermissionStatus()
                onPermissionResult?.invoke(status)
            }
        }
    }

    override fun requestPermission() {
        when(val currentStatus = currentPermissionStatus) {
            is Granted -> onPermissionResult?.invoke(Granted)
            is Denied -> if (currentStatus.shouldShowRationale) {
                locationManager.requestWhenInUseAuthorization()
            } else {
                onPermissionResult?.invoke(Denied(false))
            }
        }
    }

    override fun fetchCurrentPermissionStatus() {
        onPermissionResult?.invoke(currentPermissionStatus)
    }
}

private fun CLAuthorizationStatus.toPermissionStatusDetailed(): PermissionStatus = when(this) {
    kCLAuthorizationStatusAuthorizedAlways,
    kCLAuthorizationStatusAuthorizedWhenInUse -> Granted
    kCLAuthorizationStatusNotDetermined -> Denied(shouldShowRationale = true)
    else -> Denied(shouldShowRationale = false)
}

private fun CLAuthorizationStatus.toPermissionStatus(): PermissionStatus = when(this) {
    kCLAuthorizationStatusAuthorizedAlways,
    kCLAuthorizationStatusAuthorizedWhenInUse -> Granted
    else -> Denied(shouldShowRationale = false)
}

