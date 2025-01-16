package com.dancesurf.map

data class MapSettings(
    val isMyLocationEnabled: Boolean = true,
    val isAllGesturesEnabled: Boolean = true,
    val isScrollGesturesEnabled: Boolean = true,
    val isZoomGesturesEnabled: Boolean = true,
    val isCompassButtonEnabled: Boolean = true,
    val isMyLocationButtonEnabled: Boolean = true,
    val minCameraZoom: Float = 0.0f,
    val maxCameraZoom: Float = 25.0f
) {
    companion object {
        val default: MapSettings
            get() = MapSettings(
                isCompassButtonEnabled = false,
                isMyLocationButtonEnabled = false,
                minCameraZoom = 1.0f,
                maxCameraZoom = 20.0f
            )
    }
}