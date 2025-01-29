package com.dancesurf.system.di

import android.content.Context
import android.location.LocationManager
import com.dancesurf.system.location.SystemLocationManager
import com.dancesurf.system.location.utils.LocationServiceHealthKeeper
import com.dancesurf.system.location.utils.LocationServiceSettingsProvider
import com.google.android.gms.location.LocationServices
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual fun locationModule(): Module = module {
    factory { get<Context>().getSystemService(LocationManager::class.java) }
    factory { LocationServices.getSettingsClient(get()) }
    factory { LocationServices.getFusedLocationProviderClient(get<Context>()) }
    factory { LocationServiceSettingsProvider(locationSettingsClient = get()) }

    factory<LocationServiceHealthKeeper> {
        LocationServiceHealthKeeper(
            permissionChecker = get(),
            locationManager = get()
        )
    }

    factory {
        SystemLocationManager(
            locationProvider = get(),
            healthKeeper = get()
        )
    }
}