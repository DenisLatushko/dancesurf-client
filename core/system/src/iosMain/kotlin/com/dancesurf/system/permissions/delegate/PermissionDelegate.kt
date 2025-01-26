package com.dancesurf.system.permissions.delegate

import com.dancesurf.system.permissions.PermissionStatus

/**
 * A basic functionality for classes which contains request permission logic
 */
interface PermissionDelegate {

    val currentPermissionStatus: PermissionStatus
    var onPermissionResult: ((PermissionStatus) -> Unit)?

    /**
     * Request permission from the system
     *
     * @param onPermissionResult The callback to invoke when the permission status changes
     */
    fun requestPermission()

    /**
     * Fetch current permission status
     *
     * @param onPermissionResult The callback to invoke when the permission status received. It is
     * going to be a callback because some services does not return the permission status immediately
     */
    fun fetchCurrentPermissionStatus()
}