package com.example.compass.smartprintertest.data.route

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.compass.smartprintertest.data.setup.LocationSyncWorker
import java.util.concurrent.TimeUnit

class LocationSyncManager(private val context: Context) {

    fun start() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = PeriodicWorkRequestBuilder<LocationSyncWorker>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                "location_sync",
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
    }
}
