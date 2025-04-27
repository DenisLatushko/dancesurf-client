package com.dancesurf

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.dancesurf.system.location.SystemLocationManager
import com.dancesurf.system.location.fetchLastKnownLocation
import com.dancesurf.system.permissions.Permission.ImpreciseLocation
import com.dancesurf.system.permissions.Permission.PreciseLocation
import com.dancesurf.system.permissions.rememberMultiplePermissionsRequester
import com.dancesurf.ui.map.MapContainer
import com.dancesurf.ui.map.MapSettings
import com.dancesurf.ui.map.location.CameraLocation
import com.dancesurf.ui.map.location.Location
import com.dancesurf.ui.map.location.MarkerData
import com.dancesurf.ui.theme.AppTheme
import com.dancesurf.utils.log.Log
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
        mapSettings = MapSettings.default.copy(
            isMyLocationEnabled = isLocationPermissionGranted
        ),
        initialCameraLocation = initialCameraLocation,
        markers = listOf(
            MarkerData("1", "Marker 1", Location(53.863138, 27.594766), false),
            MarkerData("2", "Marker 2", Location(53.883074, 27.607736), false),
            MarkerData("3", "Marker 3", Location(53.905297, 27.536564), false),
            MarkerData("4", "Marker 4", Location(53.937194, 27.593738), false),
        )
    )
}