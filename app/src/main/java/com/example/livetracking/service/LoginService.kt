package com.example.livetracking.service

import com.example.livetracking.model.DriverResponse
import com.example.livetracking.repository.LoginRepository
import com.example.livetracking.retrofit.LoginInterface
import retrofit2.Response
import javax.inject.Inject

class LoginService @Inject constructor(private val loginInterface: LoginInterface):LoginRepository {
    override suspend fun login(value: String, value1: String): Response<DriverResponse> {
        return loginInterface.login(mapOf(Pair("email", value), Pair("password", value1)))
    }
}