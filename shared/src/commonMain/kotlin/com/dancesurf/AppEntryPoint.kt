package com.dancesurf

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.dancesurf.di.sharedAppModule
import com.dancesurf.system.location.SystemLocationManager
import com.dancesurf.system.location.fetchLastKnownLocation
import com.dancesurf.system.permissions.Permission.ImpreciseLocation
import com.dancesurf.system.permissions.Permission.PreciseLocation
import com.dancesurf.system.permissions.rememberMultiplePermissionsRequester
import com.dancesurf.ui.map.MapContainer
import com.dancesurf.ui.map.MapSettings
import com.dancesurf.ui.map.MapView
import com.dancesurf.ui.map.location.CameraLocation
import com.dancesurf.ui.map.location.Location
import com.dancesurf.ui.theme.AppTheme
import com.dancesurf.utils.log.Log
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject

@Composable
fun AppEntryPoint(isDebug: Boolean) {
    Log.isLoggable = isDebug

    InitApplicationEntryPoint {
        AppTheme {
            AppContent()
        }
    }

}

@Composable
expect fun InitApplicationEntryPoint(content: @Composable () -> Unit)

@Composable
internal fun AppContent() {
    val locationService = koinInject<SystemLocationManager>()
    var isLocationPermissionGranted by remember { mutableStateOf(false) }
    val permissions = rememberMultiplePermissionsRequester(ImpreciseLocation, PreciseLocation) { resultMap ->
        isLocationPermissionGranted = resultMap.values.any { it.isGranted() }
    }
    var initialCameraLocation by remember { mutableStateOf(CameraLocation.default) }

    LaunchedEffect(permissions.deniedRequesters.size) {
        isLocationPermissionGranted = permissions.deniedRequesters.isEmpty()
        if (!isLocationPermissionGranted) {
            permissions.requestPermissions()
        }
    }

    LaunchedEffect(isLocationPermissionGranted) {
        if (isLocationPermissionGranted) {
            val coords = locationService.fetchLastKnownLocation()
            initialCameraLocation = initialCameraLocation.copy(
                location = Location(coords.lat, coords.lng)
            )
        }
    }

    MapContainer(
        modifier = Modifier.fillMaxSize(),
        initialCameraLocation = initialCameraLocation,
        mapSettings = MapSettings.default.copy(
            isMyLocationEnabled = isLocationPermissionGranted
        )
    )
}