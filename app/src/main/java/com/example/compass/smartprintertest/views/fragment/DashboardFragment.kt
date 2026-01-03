package com.example.compass.smartprintertest.views.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.example.compass.smartprintertest.R
import com.example.compass.smartprintertest.basefragment.BaseViewFragment
import com.example.compass.smartprintertest.data.route.LocationSyncManager
import com.example.compass.smartprintertest.data.route.LocationTracker
import com.example.compass.smartprintertest.data.route.RouteDrawer
import com.example.compass.smartprintertest.data.setup.MapInitializer
import com.example.compass.smartprintertest.databinding.FragmentDashboardBinding
import com.example.compass.smartprintertest.utils.Extensions.isConnected
import com.example.compass.smartprintertest.utils.Extensions.showToast
import com.example.compass.smartprintertest.utils.PermissionAdHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class DashboardFragment :
    BaseViewFragment<FragmentDashboardBinding>(FragmentDashboardBinding::inflate) {

    private val mapView get() = binding!!.mapView

    private lateinit var fusedClient: FusedLocationProviderClient
    private lateinit var permissionHelper: PermissionAdHelper
    private lateinit var locationTracker: LocationTracker
    private lateinit var routeDrawer: RouteDrawer
    private lateinit var locationOverlay: MyLocationNewOverlay

    override fun onFragmentReady(view: View, savedInstanceState: Bundle?) {
        initCore()
        initMap()
        initHelpers()
        initViews()
        observeLocations()
        fetchInitialLocation()
        startSync()
    }

    // -------------------- Init --------------------
    private fun initCore() {
        fusedClient = LocationServices.getFusedLocationProviderClient(activity)
        permissionHelper = PermissionAdHelper(activity, this, prefHelper)
    }

    private fun initMap() {
        MapInitializer(mapView, activity.isConnected()).init()
        setupLocationOverlay()
    }

    private fun initHelpers() {
        routeDrawer = RouteDrawer(mapView)

        locationTracker = LocationTracker(fusedClient, permissionHelper) { location ->
            mapViewModel.saveLocation(
                location.latitude,
                location.longitude,
                location.accuracy
            )
            moveCamera(location.latitude, location.longitude)
        }
    }

    private fun startSync() {
        LocationSyncManager(requireContext()).start()
    }

    // -------------------- UI --------------------
    private fun initViews() = with(binding!!) {
        btnStart.setOnClickListener {
            locationTracker.startTracking(activity) {
                activity.showToast(getString(R.string.permission_required))
            }
        }

        btnStop.setOnClickListener {
            locationTracker.stopTracking()
            activity.showToast(getString(R.string.tracking_stopped))
        }

        btnClear.setOnClickListener {
            mapViewModel.clearRoute()
            locationTracker.clearState()
        }
    }

    // -------------------- Location --------------------
    @SuppressLint("MissingPermission")
    private fun fetchInitialLocation() {
        permissionHelper.requestLocationPermission(
            onGranted = {
                if (!permissionHelper.hasLocationPermission()) return@requestLocationPermission
                fusedClient.lastLocation.addOnSuccessListener {
                    it?.let { moveCamera(it.latitude, it.longitude) }
                }
            },
            onDenied = {
                activity.showToast(
                    getString(R.string.allow_location_permission_to)
                )
            }
        )
    }

    private fun moveCamera(lat: Double, lng: Double) {
        mapView.controller.apply {
            setZoom(18.0)
            setCenter(GeoPoint(lat, lng))
        }
    }

    // -------------------- Overlay --------------------
    private fun setupLocationOverlay() {
        locationOverlay =
            MyLocationNewOverlay(GpsMyLocationProvider(activity), mapView).apply {
                enableMyLocation()
                enableFollowLocation()
            }
        mapView.overlays.add(locationOverlay)
    }

    // -------------------- Observers --------------------
    private fun observeLocations() {
        mapViewModel.locations.observe(viewLifecycleOwner) { list ->

            // 1️⃣ Route redraw
            routeDrawer.drawRoute(list)

            if (list.isNotEmpty()) {
                val last = list.last()
                moveCamera(last.latitude, last.longitude)
                locationTracker.restoreLastLocation(last.latitude, last.longitude)
            }


        }

    }

    // -------------------- Lifecycle --------------------
    override fun onResume() {
        super.onResume()
        mapView.onResume()
        locationOverlay.enableMyLocation()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
        locationOverlay.disableMyLocation()
    }

    override fun onBackPress() {}
}
