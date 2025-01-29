package com.dancesurf.system.location.utils

import android.location.Location
import com.dancesurf.system.location.LocationCoords

internal fun Location.toLocationCoords(): LocationCoords = LocationCoords(
    lat = latitude,
    lng = longitude
)