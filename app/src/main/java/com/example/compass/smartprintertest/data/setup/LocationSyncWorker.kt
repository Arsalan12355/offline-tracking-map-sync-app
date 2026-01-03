package com.example.compass.smartprintertest.data.setup

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.compass.smartprintertest.data.roomDb.AppDatabase
import com.example.compass.smartprintertest.data.repository.LocationRepository
import com.example.compass.smartprintertest.data.service.MockApiService

class LocationSyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val dao =
        AppDatabase.getDatabase(context).locationDao()
    private val repository = LocationRepository(dao)
    private val api = MockApiService()

    override suspend fun doWork(): Result {
        return try {
            val pending = repository.getPendingLocations()

            pending.forEach {
                val response = api.uploadLocation(it)
                if (response.isSuccessful) {
                    repository.markAsSynced(it)
                }
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
