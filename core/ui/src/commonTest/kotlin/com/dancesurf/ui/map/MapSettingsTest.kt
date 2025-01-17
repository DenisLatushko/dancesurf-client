package com.dancesurf.ui.map

import com.dancesurf.ui.map.MapSettings
import kotlin.test.Test
import kotlin.test.assertEquals

class MapSettingsTest {

    @Test
    fun `given map setting when call default then return it`() {
        val actualSetting = MapSettings.default
        val expectedSettings = MapSettings(
            isMyLocationEnabled = true,
            isAllGesturesEnabled = true,
            isScrollGesturesEnabled = true,
            isZoomGesturesEnabled = true,
            isCompassButtonEnabled = false,
            isMyLocationButtonEnabled = false,
            minCameraZoom = 1.0f,
            maxCameraZoom = 20.0f
        )

        assertEquals(expectedSettings, actualSetting)
    }
}