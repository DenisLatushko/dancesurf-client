package com.dancesurf.system.permissions.utils

import android.Manifest
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import com.dancesurf.system.permissions.Permission
import com.dancesurf.system.permissions.Permission.ImpreciseLocation
import com.dancesurf.system.permissions.Permission.Notifications
import com.dancesurf.system.permissions.Permission.PreciseLocation

private fun getPermissionsMap(): List<Pair<String, Permission>> = mutableListOf(
    Manifest.permission.ACCESS_COARSE_LOCATION to ImpreciseLocation,
    Manifest.permission.ACCESS_FINE_LOCATION to PreciseLocation
).also {
    if (SDK_INT >= TIRAMISU) {
        Manifest.permission.POST_NOTIFICATIONS to Notifications
    }
}

/**
 * Map [Permission] to the platform permission [String]
 */
internal val Permission.platformPermission: String?
    get() = getPermissionsMap()
        .find { it.second == this }
        ?.first

/**
 * Map platform permission [String] to the [Permission]
 */
internal val String.applicationPermission: Permission?
    get() = getPermissionsMap()
        .find { it.first == this }
        ?.second