package com.example.livetracking.repository

import com.example.livetracking.model.RideDetails

interface LiveTrackingRepository {
    suspend fun trackRideDetails(userRideDetails: RideDetails)
    suspend fun connectSocket()
    suspend fun welcome(userRideDetails: RideDetails)
    suspend fun disconnectSocket()

//    suspend fun getLocation(): Location?
    suspend fun subscribeToTrip(tripId: String, from: String)
}