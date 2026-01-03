package com.example.compass.smartprintertest.data.repository

import com.example.compass.smartprintertest.data.roomDb.LocationDao
import com.example.compass.smartprintertest.data.roomDb.LocationEntity

class LocationRepository(private val dao: LocationDao) {

    val allLocations = dao.getAllLocations()

    suspend fun insert(location: LocationEntity) {
        dao.insert(location)
    }

    suspend fun clear() {
        dao.clear()
    }

    suspend fun getPendingLocations(): List<LocationEntity> =
        dao.getPendingLocations() // ORDER BY timestamp ASC

    suspend fun markAsSynced(location: LocationEntity) {
        dao.update(location.copy(syncStatus = "SYNCED"))
    }

}
