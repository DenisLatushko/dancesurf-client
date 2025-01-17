package com.dancesurf.ui.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dancesurf.ui.map.location.CameraLocation

@Composable
expect fun MapContainer(
    modifier: Modifier = Modifier.fillMaxSize(),
    mapSettings: MapSettings = MapSettings.default,
    initialCameraLocation: CameraLocation? = null,
)