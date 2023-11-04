package com.example.livetracking.model

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.serialization.Serializable

@Serializable
@RequiresApi(Build.VERSION_CODES.O)
data class RideDetails (
    val timestamp: Long = System.currentTimeMillis(),
    val tripId: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val speed: Float = 0.0f,
    val accuracy: Float = 0.0f,
    val direction: Float = 0.0f,
    val networkAvailable: Boolean=true
)