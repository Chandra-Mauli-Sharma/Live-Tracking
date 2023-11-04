package com.example.livetracking.model

import kotlinx.serialization.*

@Serializable
data class TripListResponse (
    val msg: String,
    val data: List<Trip>
)

@Serializable
data class Trip (
    val distance: Double,
    val id: Long,
    val name: String,
    val scheduledTime: String? = null,
    val expectedDeliverTime: String? = null,
    val source: Destination,
    val destination: Destination,
    val path: List<Path>? = null,
    val tripEnded: Boolean? = null,
    val createdAt: String,
    val updatedAt: String,

    @SerialName("CompanyId")
    val companyID: Long,

    @SerialName("DriverId")
    val driverID: Long,

    @SerialName("UserId")
    val userID: Long? = null
)

@Serializable
data class Destination (
    val lat: Double,
    val lng: Double
)

@Serializable
data class Path (
    @SerialName("tripId")
    val tripID: String,

    val timestamp: String,
    val lat: Double? = null,
    val long: Double? = null,
    val speed: Double? = null,
    val accuracy: Double? = null,
    val direction: Double? = null,
    val lng: Double? = null
)
