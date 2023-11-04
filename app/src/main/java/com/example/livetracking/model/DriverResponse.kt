package com.example.livetracking.model

import kotlinx.serialization.SerialName

data class DriverResponse (
    val msg: String,
    val data: Data
)

data class Data (
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val role: String,
    val createdAt: String,
    val updatedAt: String,

    @SerialName("CompanyId")
    val companyID: Long,

    @SerialName("Company")
    val company: Company
)

data class Company (
    val id: Long,
    val name: String,
    val createdAt: String,
    val updatedAt: String
)