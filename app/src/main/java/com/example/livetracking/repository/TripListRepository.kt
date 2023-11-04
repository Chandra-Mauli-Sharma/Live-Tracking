package com.example.livetracking.repository

import com.example.livetracking.model.TripListResponse
import retrofit2.Response

interface TripListRepository {
    suspend fun getTripList(driverID:String):Response<TripListResponse>
}