package com.example.livetracking.entity

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class RideDetail():RealmObject {
    var timestamp: Long = System.currentTimeMillis()
    var lat: Double = 0.0
    var lng: Double = 0.0
    var speed: Float = 0.0f
    var accuracy: Float = 0.0f
    var direction: Float = 0.0f
}