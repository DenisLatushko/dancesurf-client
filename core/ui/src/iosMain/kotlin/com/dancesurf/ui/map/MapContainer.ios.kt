@file:OptIn(ExperimentalForeignApi::class)

package com.dancesurf.ui.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import cocoapods.GoogleMaps.GMSMapView
import cocoapods.GoogleMaps.GMSMarker
import com.dancesurf.ui.map.location.CameraLocation
import com.dancesurf.ui.map.location.MarkerData
import com.dancesurf.ui.map.utils.MapViewDelegate
import com.dancesurf.ui.map.utils.animateWithCameraUpdate
import com.dancesurf.ui.map.utils.setCamera
import com.dancesurf.ui.map.utils.setUpSettings
import com.dancesurf.ui.map.utils.toCLLocationCoordinate2D
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun MapContainer(
    modifier: Modifier,
    mapSettings: MapSettings,
    initialCameraLocation: CameraLocation,
    markers: List<MarkerData>,
    onMarkerClick: (MarkerData) -> Unit
) {
    val mapView = remember(mapSettings) { GMSMapView() }
    val mapDelegate = remember(mapView) { MapViewDelegate() }
    var isMapSetupCompleted by remember(mapView) { mutableStateOf(false) }
    var shouldChangeCameraPosition by remember(initialCameraLocation) { mutableStateOf(true) }
    var redrawMarkers = remember(markers) { true }

    Box(modifier = modifier) {
        UIKitView(
            modifier = Modifier.fillMaxSize(),
            factory = { mapView.apply { delegate = mapDelegate } },
            update = { view ->
                with(view) {
                    if (!isMapSetupCompleted) {
                        initialCameraLocation.run { animateWithCameraUpdate(location) }
                        setUpSettings(mapSettings)
                        isMapSetupCompleted = true
                    }

                    if (shouldChangeCameraPosition) {
                        shouldChangeCameraPosition = false
                        initialCameraLocation.run { setCamera(this) }
                    }

                    if (redrawMarkers) {
                        markers.forEach { marker ->
                            GMSMarker().apply {
                                position = marker.location.toCLLocationCoordinate2D()
                                title = marker.title
                                userData = marker.id
                                map = view
                                snippet = marker.id
                            }
                        }
                        redrawMarkers = false
                    }
                }
            }
        )
    }
}