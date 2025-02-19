@file:OptIn(ExperimentalForeignApi::class)

package com.dancesurf.ui.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitInteropInteractionMode
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitView
import cocoapods.GoogleMaps.GMSMapView
import com.dancesurf.ui.map.location.CameraLocation
import com.dancesurf.ui.map.utils.MapViewDelegate
import com.dancesurf.ui.map.utils.setCamera
import com.dancesurf.ui.map.utils.setUpSettings
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class, ExperimentalComposeUiApi::class)
@Composable
actual fun MapContainer(
    modifier: Modifier,
    mapSettings: MapSettings,
    cameraLocation: CameraLocation
) {
    val mapDelegate = remember { MapViewDelegate() }
    var isCameraLocationChanged by remember(cameraLocation) { mutableStateOf(true) }

    Box(modifier = modifier) {
        UIKitView(
            modifier = Modifier.fillMaxSize(),
            properties = UIKitInteropProperties(UIKitInteropInteractionMode.NonCooperative),
            factory = {
                GMSMapView().apply {
                    delegate = mapDelegate
                    setUpSettings(mapSettings)
                    setCamera(cameraLocation)
                }
            },
            update = { map ->
                if (isCameraLocationChanged) {
                    map.setCamera(cameraLocation)
                    isCameraLocationChanged = false
                }
            },
            onRelease = { map -> map.removeFromSuperview() }
        )
    }
}