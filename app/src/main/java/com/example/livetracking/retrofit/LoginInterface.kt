package com.example.livetracking.retrofit

import com.example.livetracking.model.DriverResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.Objects

interface LoginInterface {
    @POST("/api/v1/auth/login")
    suspend fun login(@Body mapOf: Map<String, String>):Response<DriverResponse>
}