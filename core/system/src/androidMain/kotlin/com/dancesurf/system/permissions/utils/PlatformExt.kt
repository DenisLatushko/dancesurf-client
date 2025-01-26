package com.dancesurf.system.permissions.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS
import android.provider.Settings.EXTRA_APP_PACKAGE
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle.Event
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.dancesurf.system.permissions.MultiplePermissionRequester
import com.dancesurf.system.permissions.Permission
import com.dancesurf.system.permissions.Permission.Notifications
import com.dancesurf.system.permissions.PermissionStatus
import com.dancesurf.system.permissions.SinglePermissionRequester

/**
 * Find any [Activity] from the existing [Context]
 */
internal fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Context is not related to any activity")
}

/**
 * Initialize the explicit [Intent] to navigate user to the system permissions settings screen
 *
 * @param context The current [Context]
 * @param permission The [Permission] to request
 * @return The [Intent] to navigate user
 */
internal fun newPermissionsSettingsIntent(
    context: Context,
    permission: Permission
): Intent = if (permission == Notifications && SDK_INT >= TIRAMISU) {
    context.notificationsSettingsIntent
} else {
    context.permissionsSettingsIntent
}

/**
 * Initialize [Intent] to open notification settings
 */
internal val Context.notificationsSettingsIntent: Intent
    get() = Intent(ACTION_APP_NOTIFICATION_SETTINGS).apply {
        putExtra(EXTRA_APP_PACKAGE, packageName)
    }

/**
 * Initialize [Intent] to open permissions settings
 */
internal val Context.permissionsSettingsIntent: Intent
    get() = Intent().apply {
        action = ACTION_APPLICATION_DETAILS_SETTINGS
        data = Uri.fromParts("package", packageName, null)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }

/**
 * Refresh [PermissionStatus] when the application lifecycle is triggered
 *
 * @param permissionRequester The [SinglePermissionRequester] to refresh only one permission
 * @param trigger The app lifecycle [Event] to trigger the refresh
 */
@Composable
internal fun AppLifecyclePermissionController(
    permissionRequester: SinglePermissionRequester,
    trigger: Event = ON_RESUME
) {
    val lifecycleObserver = remember(permissionRequester) {
        LifecycleEventObserver { _, event ->
            if (trigger == event) {
                permissionRequester.refreshPermissionStatus()
            }
        }
    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycleObserver) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }
}

/**
 * Refresh [PermissionStatus] when the application lifecycle is triggered
 *
 * @param permissionRequester The [MultiplePermissionRequester] to refresh permissions
 * @param trigger The app lifecycle [Event] to trigger the refresh
 */
@Composable
internal fun AppLifecyclePermissionController(
    permissionRequester: MultiplePermissionRequester,
    trigger: Event = ON_RESUME
) {
    val lifecycleObserver = remember(permissionRequester) {
        LifecycleEventObserver { _, event ->
            if (trigger == event) {
                permissionRequester.refreshAllPermissionsStatus()
            }
        }
    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycleObserver) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }
}
