package com.dancesurf

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dancesurf.di.sharedAppModule
import com.dancesurf.ui.map.MapView
import com.dancesurf.ui.map.location.CameraLocation
import com.dancesurf.ui.map.location.Location
import com.dancesurf.ui.theme.AppTheme
import com.dancesurf.utils.log.Log
import org.koin.compose.KoinApplication

@Composable
fun AppEntryPoint(isDebug: Boolean) {
    Log.isLoggable = isDebug

    KoinApplication(application = { modules(sharedAppModule(isDebug)) }) {
        AppTheme {
            AppContent()
        }
    }
}

@Composable
private fun AppContent() {
    MapView(
        modifier = Modifier.fillMaxSize(),
        initialCameraLocation = CameraLocation(Location(52.24509, 20.94694), 12f)
    )
}