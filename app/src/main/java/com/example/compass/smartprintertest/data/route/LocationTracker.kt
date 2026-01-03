package com.example.compass.smartprintertest.data.route

import android.annotation.SuppressLint
import android.app.Activity
import android.location.Location
import com.example.compass.smartprintertest.utils.Extensions.showToast
import com.example.compass.smartprintertest.utils.PermissionAdHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority

class LocationTracker(
    private val fusedClient: FusedLocationProviderClient,
    private val permissionHelper: PermissionAdHelper,
    private val onLocationUpdated: (Location) -> Unit
) {

    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null
    private var lastSavedLocation: Location? = null
    var isTracking = false
        private set

    @SuppressLint("MissingPermission")
    fun startTracking(activity: Activity, onDenied: () -> Unit) {
        if (isTracking) return

        permissionHelper.requestLocationPermission(
            onGranted = {
                if (!permissionHelper.hasLocationPermission()) return@requestLocationPermission

                createLocationRequest()
                createLocationCallback()
                locationRequest?.let { locationCallback?.let { it1 ->
                    fusedClient.requestLocationUpdates(it,
                        it1, null)
                } }
                isTracking = true
                activity.showToast("Tracking Started")
            },
            onDenied = onDenied
        )
    }

    fun stopTracking() {
        if (!isTracking) return
        fusedClient.removeLocationUpdates(locationCallback!!)
        isTracking = false
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 8000L)
            .setMinUpdateDistanceMeters(10f)
            .build()
    }

    private fun createLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.locations.forEach { location ->
                    if (location.accuracy > 30f) return
                    lastSavedLocation?.let { if (location.distanceTo(it) < 10f) return }
                    lastSavedLocation = location
                    onLocationUpdated(location)
                }
            }
        }
    }

    fun restoreLastLocation(lat: Double, lng: Double) {
        lastSavedLocation = Location("").apply {
            latitude = lat
            longitude = lng
        }
    }

    fun clearState() {
        lastSavedLocation = null
    }

}
