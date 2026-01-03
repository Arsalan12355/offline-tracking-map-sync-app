package com.example.compass.smartprintertest.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.compass.smartprintertest.data.roomDb.AppDatabase
import com.example.compass.smartprintertest.data.roomDb.LocationEntity
import com.example.compass.smartprintertest.data.repository.LocationRepository
import kotlinx.coroutines.launch

class MapViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: LocationRepository
    val locations: LiveData<List<LocationEntity>>

    init {
        val dao = AppDatabase.getDatabase(application)
            .locationDao()

        repository = LocationRepository(dao)
        locations = repository.allLocations
    }

    fun saveLocation(lat: Double, lng: Double, accuracy: Float) {
        viewModelScope.launch {
            repository.insert(
                LocationEntity(
                    latitude = lat,
                    longitude = lng,
                    accuracy = accuracy,
                    syncStatus = "PENDING"
                )
            )
        }
    }

    fun clearRoute() {
        viewModelScope.launch {
            repository.clear()
        }
    }
}
