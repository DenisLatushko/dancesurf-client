package com.dancesurf.system.permissions.utils

import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString

/**
 * Do a system call to open the system settings
 */
internal fun openSystemSettingsScreen() {
    val settingsUrl = NSURL.URLWithString(UIApplicationOpenSettingsURLString) ?: return
    UIApplication.sharedApplication.openURL(settingsUrl, emptyMap<Any?, String>(), null)
}