package com.dancesurf.system.permissions

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.dancesurf.system.permissions.utils.AppLifecyclePermissionController
import com.dancesurf.system.permissions.utils.applicationPermission
import com.dancesurf.system.permissions.utils.findActivity

@Composable
actual fun rememberSinglePermissionRequester(
    permission: Permission,
    onPermissionResult: (PermissionStatus) -> Unit
): SinglePermissionRequester {
    val currentContext = LocalContext.current
    val requester = remember(permission) {
        SinglePermissionRequesterImpl(
            activity = currentContext.findActivity(),
            permission = permission,
            onPermissionResult = onPermissionResult
        )
    }

    AppLifecyclePermissionController(requester)

    val launcher = rememberLauncherForActivityResult(contract = RequestPermission()) {
        requester.refreshPermissionStatus()
        onPermissionResult(requester.permissionResult)
    }

    DisposableEffect(requester, launcher) {
        requester.launcher = launcher
        onDispose {
            requester.launcher = null
        }
    }

    return requester
}

@Composable
actual fun rememberMultiplePermissionsRequester(
    vararg permissions: Permission,
    onPermissionsResult: (Map<Permission, PermissionStatus>) -> Unit
): MultiplePermissionRequester {
    val currentContext = LocalContext.current
    val requester = remember(permissions) {
        MultiplePermissionRequesterImpl(
            activity = currentContext.findActivity(),
            permissions = permissions.asList(),
            onPermissionsResult = onPermissionsResult
        )
    }

    AppLifecyclePermissionController(requester)

    val launcher = rememberLauncherForActivityResult(contract = RequestMultiplePermissions()) { resultMap ->
        val activity = currentContext.findActivity()
        val result = resultMap
            .mapValues {
                when (it.value) {
                    true -> PermissionStatus.Granted
                    false -> PermissionStatus.Denied(
                        shouldShowRationale = activity.shouldShowRequestPermissionRationale(it.key)
                    )
                }
            }
            .mapKeys { it.key.applicationPermission }
            .filterKeys { it != null }
            .mapKeys { it.key as Permission }
        requester.refreshPermissionsStatus(result)
    }

    DisposableEffect(requester, launcher) {
        requester.launcher = launcher
        onDispose {
            requester.launcher = null
        }
    }

    return requester
}