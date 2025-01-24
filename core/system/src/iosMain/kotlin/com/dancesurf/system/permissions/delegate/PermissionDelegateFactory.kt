package com.dancesurf.system.permissions.delegate

import com.dancesurf.system.permissions.Permission
import com.dancesurf.system.permissions.Permission.ImpreciseLocation
import com.dancesurf.system.permissions.Permission.Notifications
import com.dancesurf.system.permissions.Permission.PreciseLocation

/**
 * Initialize appropriate [PermissionDelegate] for a specific permission
 *
 * @param permission The [Permission] to request
 * @return The [PermissionDelegate]
 */
internal fun getPermissionDelegate(permission: Permission): PermissionDelegate = when(permission) {
    ImpreciseLocation, PreciseLocation -> LocationPermissionDelegate()
    Notifications -> NotificationPermissionDelegate()
}