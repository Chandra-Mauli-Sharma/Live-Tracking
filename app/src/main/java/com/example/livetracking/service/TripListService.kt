package com.example.livetracking.service

import com.example.livetracking.model.TripListResponse
import com.example.livetracking.repository.TripListRepository
import com.example.livetracking.retrofit.TripListInterface
import retrofit2.Response
import javax.inject.Inject

class TripListService @Inject constructor(private val tripListInterface: TripListInterface):TripListRepository {
    override suspend fun getTripList(driverID:String): Response<TripListResponse> {
        return tripListInterface.getTripList(mapOf(Pair("driverId",driverID.toLong())))
    }

}