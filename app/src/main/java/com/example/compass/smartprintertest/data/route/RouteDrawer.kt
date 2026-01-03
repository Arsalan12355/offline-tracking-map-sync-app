package com.example.compass.smartprintertest.data.route

import android.graphics.Color
import com.example.compass.smartprintertest.data.roomDb.LocationEntity
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polyline

class RouteDrawer(private val mapView: MapView) {

    private var routeLine: Polyline? = null

    fun drawRoute(locations: List<LocationEntity>) {

        // CLEAR ROUTE
        if (locations.isEmpty()) {
            routeLine?.let {
                mapView.overlays.remove(it)
                routeLine = null
                mapView.invalidate()
                mapView.postInvalidate()
            }
            return
        }

        routeLine?.let { mapView.overlays.remove(it) }

        val points = locations.map {
            GeoPoint(it.latitude, it.longitude)
        }

        routeLine = Polyline().apply {
            setPoints(points)
            outlinePaint.color = Color.BLUE
            outlinePaint.strokeWidth = 8f
        }

        mapView.overlays.add(routeLine)
        mapView.invalidate()
    }
}
