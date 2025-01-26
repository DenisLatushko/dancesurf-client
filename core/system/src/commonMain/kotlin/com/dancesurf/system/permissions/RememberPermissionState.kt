package com.dancesurf.system.permissions

import androidx.compose.runtime.Composable

/**
 * Remember the latest [Permission] status and request it if needed.
 *
 * @param permission The [Permission] to request
 * @param onPermissionResult The callback to invoke when the permission status changes
 **/
@Composable
expect fun rememberSinglePermissionRequester(
    permission: Permission,
    onPermissionResult: (PermissionStatus) -> Unit
): SinglePermissionRequester

/**
 * Remember the latest [Permission] statuses and request  them it if needed.
 *
 * @param permissions The [Permission] list to request
 * @param onPermissionsResult The callback to invoke when the permissions statuses changed
 */
@Composable
expect fun rememberMultiplePermissionsRequester(
    vararg permissions: Permission,
    onPermissionsResult: (Map<Permission, PermissionStatus>) -> Unit
): MultiplePermissionRequester