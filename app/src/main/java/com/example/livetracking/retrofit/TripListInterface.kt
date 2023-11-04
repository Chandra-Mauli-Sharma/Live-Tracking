package com.example.livetracking.retrofit

import com.example.livetracking.model.TripListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.Objects

interface TripListInterface {
    @POST("/api/v1/trip/driver")
    suspend fun getTripList(@Body request:Map<String, Long>):Response<TripListResponse>
}