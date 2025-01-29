package com.dancesurf.system.location.utils

import com.dancesurf.system.location.LocationCoords
import com.dancesurf.system.location.LocationError

internal fun LocationCoords?.toResult(): Result<LocationCoords> = when(this) {
    null -> Result.failure(LocationError.LocationNotAvailable)
    else -> Result.success(this)
}