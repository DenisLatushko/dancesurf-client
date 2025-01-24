package com.dancesurf.system.permissions

import androidx.compose.runtime.Stable

/**
 * A basic functionality for any single permission requester. It should contain
 * all necessary methods to check and keep track of permission status.
 */
@Stable
interface SinglePermissionRequester {
    val permission: Permission
    val permissionResult: PermissionStatus
    fun requestPermission()
    fun openSystemSettings()
    fun refreshPermissionStatus()
}

/**
 * A basic functionality for any multiple permissions requester. It should contain
 * all necessary methods to check and keep track of permission status.
 */
@Stable
interface MultiplePermissionRequester {
    val allRequesters: List<SinglePermissionRequester>
    val grantedRequesters: List<SinglePermissionRequester>
    val deniedRequesters: List<SinglePermissionRequester>
    @Throws(IllegalStateException::class) fun requestPermissions()
    fun openSystemSettings()
    fun refreshPermissionsStatus(permissionResultMap: Map<Permission, PermissionStatus>)
    fun refreshAllPermissionsStatus()
}

/**
 * Convert [SinglePermissionRequester] list to map
 */
internal fun List<SinglePermissionRequester>.toMap(): Map<Permission, PermissionStatus> =
    associate { requester -> requester.permission to requester.permissionResult }

/**
 * Clear the existing values and add new
 *
 * @param requesters [SinglePermissionRequester] list to bew added
 * @return a size of the updated map
 */
internal fun MutableMap<Permission, SinglePermissionRequester>.associateWith(
    requesters: List<SinglePermissionRequester>
): Int {
    clear()
    requesters.forEach { requester -> put(requester.permission, requester) }
    return size
}