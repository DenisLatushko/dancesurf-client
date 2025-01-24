package com.dancesurf.system.permissions

import androidx.compose.runtime.Stable
import com.dancesurf.system.permissions.utils.clearExcess
import com.dancesurf.system.permissions.utils.openSystemSettingsScreen

@Stable
internal class MultiplePermissionRequesterImpl(
    permissions: List<Permission>,
    private val onPermissionsResult: (Map<Permission, PermissionStatus>) -> Unit,
) : MultiplePermissionRequester {

    private val activeRequesters = mutableMapOf<Permission, SinglePermissionRequester>()
    private var activeRequestersCounter = activeRequesters.size

    override val allRequesters: List<SinglePermissionRequester> = permissions
        .clearExcess()
        .map { permission ->
            SinglePermissionRequesterImpl(
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
            onPermissionsResult(allRequesters.toMap())
        } else {
            activeRequestersCounter = activeRequesters.associateWith(deniedRequesters)
            deniedRequesters.forEach { requester -> requester.requestPermission() }
        }
    }

    override fun openSystemSettings() {
        openSystemSettingsScreen()
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