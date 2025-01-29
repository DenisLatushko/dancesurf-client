package com.dancesurf.system.permissions

import kotlin.coroutines.suspendCoroutine

/**
 * A wrapper to fetch the actual value of the required [Permission]
 */
expect class PermissionChecker {
    fun fetchPermissionStatus(permission: Permission, onFetched: (Boolean) -> Unit)
}

suspend fun PermissionChecker.fetchPermissionStatus(permission: Permission): Boolean =
    suspendCoroutine { continuation ->
        fetchPermissionStatus(permission) { isGranted ->
            continuation.resumeWith(Result.success(isGranted))
        }
    }