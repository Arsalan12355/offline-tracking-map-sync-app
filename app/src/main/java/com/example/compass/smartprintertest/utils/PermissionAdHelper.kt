package com.example.compass.smartprintertest.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment


class PermissionAdHelper(
    private val activity: Activity,
    fragment: Fragment,
    private val prefHelper: PrefHelper
) {

    private var onGranted: (() -> Unit)? = null
    private var onDenied: (() -> Unit)? = null

    private val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private val requestPermissionLauncher =
        fragment.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val allGranted = permissions.values.all { it }

            if (allGranted) {
                onGranted?.invoke()
            } else {
                prefHelper.locationDenyCount += 1
                if (prefHelper.locationDenyCount >= 2) {
                    showSystemPermissionDialog()
                } else {
                    onDenied?.invoke()
                }
            }
        }

    private val settingsLauncher =
        fragment.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (hasLocationPermission()) {
                onGranted?.invoke()
            } else {
                onDenied?.invoke()
            }
        }

    fun hasLocationPermission(): Boolean {
        return locationPermissions.all {
            ContextCompat.checkSelfPermission(
                activity,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun requestLocationPermission(
        onGranted: () -> Unit,
        onDenied: () -> Unit = {}
    ) {
        this.onGranted = onGranted
        this.onDenied = onDenied

        if (hasLocationPermission()) {
            onGranted()
        } else {
            requestPermissionLauncher.launch(locationPermissions)
        }
    }


    private fun showSystemPermissionDialog() {
        AlertDialog.Builder(activity)
            .setTitle("Location Permission Required")
            .setMessage(
                "This app needs location permission to work properly. " +
                        "Please allow it from app settings."
            )
            .setCancelable(false)
            .setPositiveButton("Open Settings") { _, _ ->
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", activity.packageName, null)
                )
                settingsLauncher.launch(intent)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                onDenied?.invoke()
            }
            .show()
    }


}



