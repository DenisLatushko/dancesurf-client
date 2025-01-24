package com.dancesurf.system.permissions.utils

import com.dancesurf.system.permissions.Permission
import com.dancesurf.system.permissions.Permission.PreciseLocation
import com.dancesurf.system.permissions.Permission.ImpreciseLocation

/**
 * Some [Permission] objects can lead to initialization of the same [PermissionDelegate]
 */
internal fun List<Permission>.clearExcess(): List<Permission> {
    val distinctPermissions = mutableListOf(*this.toTypedArray())
    if (distinctPermissions.containsAll(listOf(PreciseLocation, ImpreciseLocation))) {
        distinctPermissions.remove(PreciseLocation)
    }
    return distinctPermissions
}