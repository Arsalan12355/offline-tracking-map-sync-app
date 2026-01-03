package com.example.compass.smartprintertest.data.service

import com.example.compass.smartprintertest.data.roomDb.LocationEntity
import kotlinx.coroutines.delay
import retrofit2.Response

class MockApiService {

    suspend fun uploadLocation(location: LocationEntity): Response<Unit> {
        delay(300) // simulate network delay
        return Response.success(Unit)
    }
}
