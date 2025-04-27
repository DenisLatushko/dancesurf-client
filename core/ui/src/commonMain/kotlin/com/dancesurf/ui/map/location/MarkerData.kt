package com.dancesurf.ui.map.location

class MarkerData(
    val id: String,
    val title: String,
    val location: Location,
    val isSelected: Boolean,
    val iconUrl: String? = null
)