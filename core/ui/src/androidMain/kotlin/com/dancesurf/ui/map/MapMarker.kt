package com.dancesurf.ui.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.dancesurf.ui.map.location.MarkerData
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@Composable
internal fun MapMarker(
    marker: MarkerData,
) {
    val markerState = remember {
        MarkerState.invoke(position = marker.location.let { LatLng(it.lat, it.lng) })
    }

    Marker(
        state = markerState,
        contentDescription = marker.title,
        visible = true,
        title = marker.title
    )
}