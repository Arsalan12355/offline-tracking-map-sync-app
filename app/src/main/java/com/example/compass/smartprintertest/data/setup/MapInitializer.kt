package com.example.compass.smartprintertest.data.setup

import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

class MapInitializer(
    private val mapView: MapView,
    private val isConnected: Boolean
) {
    fun init() {
        mapView.setMultiTouchControls(true)
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.controller.setZoom(18.0)

        if (!isConnected) mapView.setUseDataConnection(false)

        mapView.controller.setCenter(GeoPoint(24.8607, 67.0011))
    }
}
