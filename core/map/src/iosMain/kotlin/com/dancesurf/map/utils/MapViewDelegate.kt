package com.dancesurf.map.utils

import cocoapods.GoogleMaps.GMSMapView
import cocoapods.GoogleMaps.GMSMapViewDelegateProtocol
import cocoapods.GoogleMaps.GMSMarker
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreLocation.CLLocationCoordinate2D
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
internal class MapViewDelegate(
    private val onTapAtCoordinate: ((GMSMapView, CValue<CLLocationCoordinate2D>) -> Unit)? = null,
    private val onTapMarker: ((GMSMapView, GMSMarker) -> Unit)? = null
): NSObject(), GMSMapViewDelegateProtocol {

    override fun mapView(
        mapView: GMSMapView,
        didTapAtCoordinate: CValue<CLLocationCoordinate2D>
    ) {
        onTapAtCoordinate?.invoke(mapView, didTapAtCoordinate)
    }

    override fun mapView(mapView: GMSMapView, didTapMarker: GMSMarker): Boolean {
        onTapMarker?.invoke(mapView, didTapMarker)
        return true
    }
}