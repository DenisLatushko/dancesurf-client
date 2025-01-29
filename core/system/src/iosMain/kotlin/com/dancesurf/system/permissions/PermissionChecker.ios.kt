package com.dancesurf.system.permissions

import com.dancesurf.system.permissions.delegate.getPermissionDelegate

actual class PermissionChecker {
    actual fun fetchPermissionStatus(permission: Permission, onFetched: (Boolean) -> Unit) {
        getPermissionDelegate(permission)
            .apply { onPermissionResult = { onFetched(it is PermissionStatus.Granted) } }
            .fetchCurrentPermissionStatus()
    }
}