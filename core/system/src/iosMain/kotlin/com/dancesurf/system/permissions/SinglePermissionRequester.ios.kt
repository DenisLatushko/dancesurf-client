package com.dancesurf.system.permissions

import androidx.compose.runtime.Stable
import com.dancesurf.system.permissions.delegate.getPermissionDelegate
import com.dancesurf.system.permissions.utils.openSystemSettingsScreen

@Stable
internal class SinglePermissionRequesterImpl(
    override val permission: Permission,
    private val onPermissionResult: (PermissionStatus) -> Unit
) : SinglePermissionRequester {

    private val onPermissionResultCallback: (PermissionStatus) -> Unit = {
        _permissionResult = it
        onPermissionResult(it)
    }
    private val permissionDelegate = getPermissionDelegate(permission).apply {
        onPermissionResult = onPermissionResultCallback
    }
    private var _permissionResult: PermissionStatus = permissionDelegate.currentPermissionStatus
    override val permissionResult: PermissionStatus get() = _permissionResult

    override fun requestPermission() {
        permissionDelegate.requestPermission()
    }

    override fun openSystemSettings() {
        openSystemSettingsScreen()
    }

    override fun refreshPermissionStatus() {
        permissionDelegate.fetchCurrentPermissionStatus()
    }
}