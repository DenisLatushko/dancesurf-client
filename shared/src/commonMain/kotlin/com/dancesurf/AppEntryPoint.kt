package com.dancesurf

import androidx.compose.runtime.Composable
import com.dancesurf.ui.map.MapContainer
import com.dancesurf.ui.map.location.CameraLocation
import com.dancesurf.ui.map.location.Location
import com.dancesurf.ui.theme.AppTheme

@Composable
fun AppEntryPoint() {
    AppTheme {
        MapContainer(
            initialCameraLocation = CameraLocation(
                location = Location(
                    lat = 52.245090,
                    lng = 20.946945
                ),
                zoom = 12f
            )
        )
    }
}