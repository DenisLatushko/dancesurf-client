package com.dancesurf.system.di

import com.dancesurf.system.permissions.PermissionChecker
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual fun permissionModule(): Module = module {
    factory { PermissionChecker(get()) }
}