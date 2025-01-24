package com.dancesurf.system.permissions.delegate

import com.dancesurf.system.permissions.PermissionStatus
import com.dancesurf.system.permissions.PermissionStatus.Denied
import com.dancesurf.system.permissions.PermissionStatus.Granted
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNAuthorizationStatus
import platform.UserNotifications.UNAuthorizationStatusAuthorized
import platform.UserNotifications.UNAuthorizationStatusEphemeral
import platform.UserNotifications.UNAuthorizationStatusNotDetermined
import platform.UserNotifications.UNAuthorizationStatusProvisional
import platform.UserNotifications.UNUserNotificationCenter

/**
 * A delegate for requesting notification permission
 */
internal class NotificationPermissionDelegate : PermissionDelegate {

    private val notificationCenter = UNUserNotificationCenter.currentNotificationCenter()

    override val currentPermissionStatus: PermissionStatus = Denied(false)
    override var onPermissionResult: ((PermissionStatus) -> Unit)? = null

    override fun requestPermission() {
        getCurrentPermissionStatus { status ->
            when (status) {
                Granted, Denied(false) -> onPermissionResult?.invoke(status)
                else -> fetchCurrentPermissionStatus()
            }
        }
    }

    override fun fetchCurrentPermissionStatus() {
        notificationCenter.requestAuthorizationWithOptions(
            UNAuthorizationOptionSound
                .or(UNAuthorizationOptionAlert)
                .or(UNAuthorizationOptionBadge)
        ) { isSuccess, err ->
            if (isSuccess && err == null) {
                onPermissionResult?.invoke(Granted)
            } else {
                onPermissionResult?.invoke(Denied(false))
            }
        }
    }

    private fun getCurrentPermissionStatus(onPermissionResult: (PermissionStatus) -> Unit) {
        notificationCenter.getNotificationSettingsWithCompletionHandler { settings ->
            onPermissionResult(settings?.authorizationStatus.toPermissionStatus())
        }
    }
}

private fun UNAuthorizationStatus?.toPermissionStatus(): PermissionStatus = when (this) {
    UNAuthorizationStatusAuthorized,
    UNAuthorizationStatusProvisional,
    UNAuthorizationStatusEphemeral -> Granted
    UNAuthorizationStatusNotDetermined -> Denied(shouldShowRationale = true)
    else -> Denied(shouldShowRationale = false)
}