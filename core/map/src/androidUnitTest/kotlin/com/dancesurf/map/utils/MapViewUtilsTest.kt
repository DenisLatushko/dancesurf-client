package com.dancesurf.map.utils

import com.dancesurf.map.MapSettings
import com.dancesurf.map.location.CameraLocation
import com.dancesurf.map.location.Location
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import kotlin.test.Test
import kotlin.test.assertEquals

private const val LOCATION_LAT = 10.01
private const val LOCATION_LNG = 20.02
private const val CAMERA_ZOOM = 0.005f

class MapViewUtilsTest {

    @Test
    fun `given map settings when map ui settings needed then map settings mapped`() {
        val actualMapUiSettings = MapSettings.default.toMapUiSettings()
        val expectedMapUiSettings = MapUiSettings(
            mapToolbarEnabled = false,
            zoomControlsEnabled = false,
            zoomGesturesEnabled = true,
            scrollGesturesEnabled = true,
            myLocationButtonEnabled = false,
            compassEnabled = false
        )

        assertEquals(expectedMapUiSettings, actualMapUiSettings)
    }

    @Test
    fun `given map settings when map properties needed then map settings mapped`() {
        val actualMapProperties = MapSettings.default.toMapProperties()
        val expectedMapUiProperties = MapProperties(
            isMyLocationEnabled = true,
            minZoomPreference = 1.0f,
            maxZoomPreference = 20.0f
        )

        assertEquals(expectedMapUiProperties, actualMapProperties)
    }

    @Test
    fun `given location when prepare system LatLng then it mapped from the original object`() {
        val actualLatLng = Location(lat = LOCATION_LAT, lng = LOCATION_LNG).toLatLng()
        val expectedLatLng = LatLng(LOCATION_LAT, LOCATION_LNG)

        assertEquals(expectedLatLng, actualLatLng)
    }

    @Test
    fun `given camera location when convert to camera position then it mapped from the original object`() {
        val actualCameraPosition = CameraLocation(
            location = Location(lat = LOCATION_LAT, lng = LOCATION_LNG),
            zoom = CAMERA_ZOOM
        ).toCameraPosition()

        val expectedCameraPosition = CameraPosition.fromLatLngZoom(
            LatLng(LOCATION_LAT, LOCATION_LNG),
            CAMERA_ZOOM
        )

        assertEquals(expectedCameraPosition, actualCameraPosition)
    }

    @Test
    fun `given no location when camera position needed then default object returned`() {
        val expectedLatLng = CameraPosition.fromLatLngZoom(
            LatLng(.0, .0), 12f)

        assertEquals(expectedLatLng, cameraPositionDefault)
    }
}
