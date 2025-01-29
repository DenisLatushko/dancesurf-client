package com.dancesurf.system.location.utils

import com.dancesurf.system.location.LocationCoords
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreLocation.CLLocationCoordinate2D

@OptIn(ExperimentalForeignApi::class)
internal fun CValue<CLLocationCoordinate2D>.toLocationCoords(): LocationCoords = useContents {
    LocationCoords(
        lat = latitude,
        lng = longitude
    )
}