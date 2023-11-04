package com.example.livetracking.repository

import com.example.livetracking.model.DriverResponse
import retrofit2.Response
import java.util.Objects

interface LoginRepository {
    suspend fun login(value: String, value1: String):Response<DriverResponse>
}