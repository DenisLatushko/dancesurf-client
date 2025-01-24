package com.dancesurf.system.permissions

import android.app.Activity
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Stable
import com.dancesurf.system.permissions.utils.permissionsSettingsIntent
import com.dancesurf.system.permissions.utils.platformPermission

@Stable
internal class MultiplePermissionRequesterImpl(
    private val activity: Activity,
    private val permissions: List<Permission>,
    private val onPermissionsResult: (Map<Permission, PermissionStatus>) -> Unit
) : MultiplePermissionRequester {

    var launcher: ActivityResultLauncher<Array<String>>? = null

    private val activeRequesters = mutableMapOf<Permission, SinglePermissionRequester>()
    private var activeRequestersCounter = activeRequesters.size

    override val allRequesters = permissions.map { permission ->
        SinglePermissionRequesterImpl(
            activity = activity,
            permission = permission,
            onPermissionResult = { onSingleResult(permission) }
        )
    }

    override val grantedRequesters: List<SinglePermissionRequester>
        get() = allRequesters.filter { requester -> requester.permissionResult.isGranted() }

    override val deniedRequesters: List<SinglePermissionRequester>
        get() = allRequesters.filter { requester -> requester.permissionResult.isDenied() }

    private fun onSingleResult(permission: Permission) {
        if (activeRequesters.containsKey(permission)) activeRequestersCounter--

        if (activeRequestersCounter == 0) {
            onPermissionsResult(allRequesters.toMap())
        }
    }

    override fun requestPermissions() {
        if (grantedRequesters.size == allRequesters.size) {
            onPermissionsResult(
                allRequesters.associate { requester ->
                    requester.permission to PermissionStatus.Granted
                }
            )
        } else {
            launcher?.launch(permissions.toPlatformPermissionsArray())
                ?: throw IllegalStateException("MultiplePermissionRequesterImpl has no launcher")
        }
    }

    override fun openSystemSettings() {
        activity.startActivity(activity.permissionsSettingsIntent)
    }

    override fun refreshPermissionsStatus(permissionResultMap: Map<Permission, PermissionStatus>) {
        allRequesters
            .filter { permissionResultMap.containsKey(it.permission) }
            .refresh()
    }

    override fun refreshAllPermissionsStatus() {
        allRequesters.refresh()
    }

    private fun List<SinglePermissionRequester>.refresh() {
        activeRequestersCounter = activeRequesters.associateWith(this)
        forEach { requester -> requester.refreshPermissionStatus() }
    }
}

private fun List<Permission>.toPlatformPermissionsArray(): Array<String> =
    mapNotNull { it.platformPermission }.toTypedArray()