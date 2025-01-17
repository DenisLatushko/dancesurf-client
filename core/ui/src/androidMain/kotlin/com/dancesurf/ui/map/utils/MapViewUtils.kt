package com.dancesurf.ui.map.utils

import com.dancesurf.ui.map.MapSettings
import com.dancesurf.ui.map.location.CameraLocation
import com.dancesurf.ui.map.location.Location
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings

internal fun MapSettings.toMapUiSettings(): MapUiSettings = MapUiSettings(
    mapToolbarEnabled = false,
    zoomControlsEnabled = false,
    zoomGesturesEnabled = isZoomGesturesEnabled,
    scrollGesturesEnabled = isScrollGesturesEnabled,
    myLocationButtonEnabled = isMyLocationButtonEnabled,
    compassEnabled = isCompassButtonEnabled
)

internal fun MapSettings.toMapProperties(): MapProperties = MapProperties(
    isMyLocationEnabled = isMyLocationEnabled,
    minZoomPreference = minCameraZoom,
    maxZoomPreference = maxCameraZoom
)

internal fun Location.toLatLng(): LatLng = LatLng(lat, lng)

internal fun CameraLocation.toCameraPosition(): CameraPosition =
    CameraPosition.fromLatLngZoom(location.toLatLng(), zoom)

internal val cameraPositionDefault: CameraPosition
    get() = CameraPosition.fromLatLngZoom(LatLng(.0, .0), 12f)
