package com.dancesurf.system.permissions

/**
 * A status which presents the current permission state
 */
sealed class PermissionStatus {

    data object Granted: PermissionStatus()

    data class Denied(val shouldShowRationale: Boolean): PermissionStatus()

    fun isGranted(): Boolean = this is Granted

    fun isDenied(): Boolean = this is Denied
}