package com.dancesurf.system.permissions

import android.app.Activity
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Stable
import androidx.core.content.ContextCompat.checkSelfPermission
import com.dancesurf.system.permissions.PermissionStatus.Denied
import com.dancesurf.system.permissions.PermissionStatus.Granted
import com.dancesurf.system.permissions.utils.newPermissionsSettingsIntent
import com.dancesurf.system.permissions.utils.platformPermission

@Stable
internal class SinglePermissionRequesterImpl(
    override val permission: Permission,
    private val onPermissionResult: (PermissionStatus) -> Unit,
    private val activity: Activity,
) : SinglePermissionRequester {

    private val platformPermission = permission.platformPermission
    private var _permissionResult = getPermissionStatus()
    override val permissionResult: PermissionStatus get() = _permissionResult

    var launcher: ActivityResultLauncher<String>? = null

    override fun requestPermission() {
        if (hasPermission()) {
            refreshPermissionStatus()
        } else {
            platformPermission?.run {
                launcher?.launch(this)
                    ?: throw IllegalStateException("PermissionRequesterImpl has no launcher")
            }
        }
    }

    override fun openSystemSettings() {
        activity.startActivity(
            newPermissionsSettingsIntent(
                context = activity,
                permission = permission
            )
        )
    }

    private fun hasPermission(): Boolean = platformPermission
        ?.let { checkSelfPermission(activity, it) == PERMISSION_GRANTED }
        ?: false

    override fun refreshPermissionStatus() {
        _permissionResult = getPermissionStatus()
        onPermissionResult(_permissionResult)
    }

    private fun getPermissionStatus(): PermissionStatus = when (hasPermission()) {
        true -> Granted
        false -> {
            val showRationale = platformPermission
                ?.let { activity.shouldShowRequestPermissionRationale(it) }
                ?: false
            Denied(shouldShowRationale = showRationale)
        }
    }
}