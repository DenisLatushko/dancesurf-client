package com.dancesurf.system.di

import com.dancesurf.system.location.SystemLocationManager
import com.dancesurf.system.location.utils.LocationServiceHealthKeeper
import com.dancesurf.system.permissions.delegate.LocationPermissionDelegate
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.CoreLocation.CLLocationManager

internal actual fun locationModule(): Module = module {
   factory { CLLocationManager() }
   factory { LocationServiceHealthKeeper(locationManager = get(), permissionChecker = get()) }
   factory { SystemLocationManager(locationManager = get(), healthKeeper = get()) }
}