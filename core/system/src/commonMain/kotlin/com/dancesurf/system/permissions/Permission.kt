package com.dancesurf.system.permissions

import androidx.compose.runtime.Immutable

/**
 * Permissions list
 */
@Immutable
enum class Permission {
    ImpreciseLocation,
    PreciseLocation,
    Notifications
}