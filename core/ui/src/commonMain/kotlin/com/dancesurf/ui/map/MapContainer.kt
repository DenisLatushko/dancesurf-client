package com.dancesurf.ui.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.dancesurf.system.permissions.Permission.ImpreciseLocation
import com.dancesurf.system.permissions.Permission.PreciseLocation
import com.dancesurf.system.permissions.rememberMultiplePermissionsRequester
import com.dancesurf.ui.map.location.CameraLocation

@Composable
expect fun MapContainer(
    modifier: Modifier = Modifier,
    mapSettings: MapSettings = MapSettings.default,
    initialCameraLocation: CameraLocation,
)

@Composable
fun MapView(
    modifier: Modifier = Modifier,
    initialCameraLocation: CameraLocation,
) {
    var isLocationPermissionGranted by remember { mutableStateOf(false) }
    val permissions = rememberMultiplePermissionsRequester(ImpreciseLocation, PreciseLocation) { resultMap ->
        isLocationPermissionGranted = resultMap.values.any { it.isGranted() }
    }

    LaunchedEffect(permissions.deniedRequesters.size) {
        isLocationPermissionGranted = permissions.deniedRequesters.isEmpty()

        if (!isLocationPermissionGranted) {
            permissions.requestPermissions()
        }
    }

    MapContainer(
        modifier = modifier,
        initialCameraLocation = initialCameraLocation,
        mapSettings = MapSettings.default.copy(
            isMyLocationEnabled = isLocationPermissionGranted
        )
    )
}