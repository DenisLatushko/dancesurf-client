package com.dancesurf.ui.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dancesurf.ui.map.location.CameraLocation

@Composable
expect fun MapContainer(
    modifier: Modifier = Modifier,
    mapSettings: MapSettings = MapSettings.default,
    cameraLocation: CameraLocation,
)