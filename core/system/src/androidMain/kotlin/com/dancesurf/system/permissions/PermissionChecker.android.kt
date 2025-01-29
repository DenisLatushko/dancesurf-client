package com.dancesurf.system.permissions

import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import com.dancesurf.system.permissions.utils.platformPermission

actual class PermissionChecker(private val context: Context) {
    actual fun fetchPermissionStatus(permission: Permission, onFetched: (Boolean) -> Unit) {
        permission.platformPermission
            ?.run { onFetched(context.checkSelfPermission(this) == PERMISSION_GRANTED) }
            ?: throw IllegalStateException("Permission not found")
    }
}