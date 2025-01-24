package com.dancesurf.system.permissions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
actual fun rememberSinglePermissionRequester(
    permission: Permission,
    onPermissionResult: (PermissionStatus) -> Unit
): SinglePermissionRequester {
    val requester = rememberSaveable(permission) {
        SinglePermissionRequesterImpl(
            permission = permission,
            onPermissionResult = onPermissionResult
        )
    }
    return requester
}

@Composable
actual fun rememberMultiplePermissionsRequester(
    vararg permissions: Permission,
    onPermissionsResult: (Map<Permission, PermissionStatus>) -> Unit
): MultiplePermissionRequester {
    val requester = rememberSaveable(permissions) {
        MultiplePermissionRequesterImpl(
            permissions = permissions.asList(),
            onPermissionsResult = onPermissionsResult
        )
    }
    return requester
}