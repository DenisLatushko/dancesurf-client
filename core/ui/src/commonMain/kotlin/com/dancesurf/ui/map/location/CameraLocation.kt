package com.dancesurf.ui.map.location

private const val DEFAULT_MOVE_CAMERA_DURATION = 1000

data class CameraLocation(
    val location: Location,
    val zoom: Float,
    val durationMillis: Int = DEFAULT_MOVE_CAMERA_DURATION
) {
    companion object {
        val default = CameraLocation(
            location = Location(.0, .0),
            zoom = 12.0f
        )
    }
}