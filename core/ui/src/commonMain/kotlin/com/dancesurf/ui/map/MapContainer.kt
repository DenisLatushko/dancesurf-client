package com.dancesurf.ui.map

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
import com.dancesurf.ui.map.location.MarkerData

@Composable
expect fun MapContainer(
    modifier: Modifier = Modifier,
    mapSettings: MapSettings = MapSettings.default,
    initialCameraLocation: CameraLocation,
    markers: List<MarkerData> = emptyList(),
    onMarkerClick: (MarkerData) -> Unit = {}
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
        mapSettings = MapSettings.default.copy(
            isMyLocationEnabled = isLocationPermissionGranted
        ),
        initialCameraLocation = initialCameraLocation
    )
}