package com.dancesurf.ui.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.dancesurf.ui.map.location.CameraLocation
import com.dancesurf.ui.map.utils.toCameraPosition
import com.dancesurf.ui.map.utils.toCameraUpdate
import com.dancesurf.ui.map.utils.toMapProperties
import com.dancesurf.ui.map.utils.toMapUiSettings
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
actual fun MapContainer(
    modifier: Modifier,
    mapSettings: MapSettings,
    cameraLocation: CameraLocation
) {
    val cameraPositionState = rememberCameraPositionState {
        position = cameraLocation.toCameraPosition()
    }

    LaunchedEffect(cameraLocation) {
        cameraPositionState.animate(
            update = cameraLocation.toCameraUpdate(),
            durationMs = cameraLocation.durationMillis
        )
    }

    Box(modifier = modifier) {
        GoogleMap(
            cameraPositionState = cameraPositionState,
            modifier = Modifier.fillMaxSize(),
            uiSettings = mapSettings.toMapUiSettings(),
            properties = mapSettings.toMapProperties()
        )
    }
}