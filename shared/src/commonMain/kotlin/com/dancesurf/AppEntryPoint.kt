package com.dancesurf

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dancesurf.system.location.SystemLocationManager
import com.dancesurf.system.location.fetchLastKnownLocation
import com.dancesurf.system.permissions.Permission.ImpreciseLocation
import com.dancesurf.system.permissions.Permission.PreciseLocation
import com.dancesurf.system.permissions.rememberMultiplePermissionsRequester
import com.dancesurf.ui.layout.ChipsLayout
import com.dancesurf.ui.map.MapContainer
import com.dancesurf.ui.map.MapSettings
import com.dancesurf.ui.map.location.CameraLocation
import com.dancesurf.ui.map.location.Location
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
    var cameraLocation by remember {
        mutableStateOf(
            CameraLocation.default.copy(
                location = Location(52.228662, 21.004117)
            )
        )
    }

    LaunchedEffect(permissions.deniedRequesters.size) {
        isLocationPermissionGranted = permissions.deniedRequesters.isEmpty()
        if (!isLocationPermissionGranted) {
            permissions.requestPermissions()
        }
    }

    LaunchedEffect(isLocationPermissionGranted) {
        if (isLocationPermissionGranted) {
            val coords = locationService.fetchLastKnownLocation()
            cameraLocation = cameraLocation.copy(
                location = Location(coords.lat, coords.lng)
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        MapContainer(
            modifier = Modifier.fillMaxSize(),
            cameraLocation = cameraLocation,
            mapSettings = MapSettings.default.copy(
                isMyLocationEnabled = true
            )
        )

        ChipsLayout(
            modifier = Modifier.fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 48.dp),
            maxLines = 2,
            horizontalArrangement = Arrangement.spacedBy(
                space = 4.dp,
                alignment = Alignment.CenterHorizontally
            ),
            colors = FilterChipDefaults.filterChipColors().copy(
                containerColor = Color.White,
                selectedContainerColor = Color.Gray
            ),
            items = listOf(
                "asdasgdg", "13sdfsfdfds2", "13sd", "13sdfsfdds2",
                "adfsgdg", "13sdfsfdfds2", "13sd", "13sdfdfds2",
                "asdasdsgdg", "13fsfdfds2", "13sd", "13",
                "asdasdfsgdg", "13sdfsfdfds2", "13sd", "13sdfsfdfds2"
            ),
            overflowContent = {
                Text(text = "...")
            },
            content = {
                Text(
                    modifier = Modifier.widthIn(min = 20.dp, max = 56.dp),
                    text = it,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelSmall
                )
            },
            onSelected = { _, _ -> }
        )
    }
}