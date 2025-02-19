package com.dancesurf.ui.map.utils

import cocoapods.GoogleMaps.GMSCameraPosition
import cocoapods.GoogleMaps.GMSCameraUpdate
import cocoapods.GoogleMaps.GMSMapView
import cocoapods.GoogleMaps.animateWithCameraUpdate
import com.dancesurf.ui.map.MapSettings
import com.dancesurf.ui.map.location.CameraLocation
import com.dancesurf.ui.map.location.Location
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreLocation.CLLocationCoordinate2D
import platform.CoreLocation.CLLocationCoordinate2DMake

@OptIn(ExperimentalForeignApi::class)
internal fun GMSMapView.setUpSettings(
    mapSettings: MapSettings
) {
    myLocationEnabled = mapSettings.isMyLocationEnabled
    setMinZoom(mapSettings.minCameraZoom, mapSettings.maxCameraZoom)

    settings.apply {
        setAllGesturesEnabled(mapSettings.isAllGesturesEnabled)
        setScrollGestures(mapSettings.isScrollGesturesEnabled)
        setZoomGestures(mapSettings.isZoomGesturesEnabled)
        setCompassButton(mapSettings.isCompassButtonEnabled)
        consumesGesturesInView = true
        myLocationButton = mapSettings.isMyLocationButtonEnabled
    }
}

@OptIn(ExperimentalForeignApi::class)
internal fun GMSMapView.animateWithCameraUpdate(location: Location) {
    animateWithCameraUpdate(
        cameraUpdate = GMSCameraUpdate.setTarget(location.toCLLocationCoordinate2D())
    )
}

@OptIn(ExperimentalForeignApi::class)
internal fun GMSMapView.setCamera(cameraLocation: CameraLocation) {
    setCamera(
        camera = GMSCameraPosition.cameraWithLatitude(
            latitude = cameraLocation.location.lat,
            longitude = cameraLocation.location.lng,
            zoom = cameraLocation.zoom
        )
    )
}

@OptIn(ExperimentalForeignApi::class)
internal fun Location.toCLLocationCoordinate2D(): CValue<CLLocationCoordinate2D> =
    CLLocationCoordinate2DMake(
        latitude = lat,
        longitude = lng
    )