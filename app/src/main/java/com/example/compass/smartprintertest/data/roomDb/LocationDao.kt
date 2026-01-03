package com.example.compass.smartprintertest.data.roomDb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(location: LocationEntity)

    @Query("SELECT * FROM locations ORDER BY timestamp ASC")
    fun getAllLocations(): LiveData<List<LocationEntity>>

    @Query("SELECT * FROM locations WHERE syncStatus = 'PENDING' ORDER BY timestamp ASC")
    suspend fun getPendingLocations(): List<LocationEntity>

    @Update
    suspend fun update(location: LocationEntity)

    @Query("DELETE FROM locations")
    suspend fun clear()

}
