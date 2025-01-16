package com.dancesurf.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dancesurf.map.location.CameraLocation
import com.dancesurf.map.utils.cameraPositionDefault
import com.dancesurf.map.utils.toCameraPosition
import com.dancesurf.map.utils.toMapProperties
import com.dancesurf.map.utils.toMapUiSettings
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
actual fun MapContainer(
    modifier: Modifier,
    mapSettings: MapSettings,
    initialCameraLocation: CameraLocation?
) {
    val cameraPositionState = rememberCameraPositionState {
        position = initialCameraLocation?.toCameraPosition() ?: cameraPositionDefault
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