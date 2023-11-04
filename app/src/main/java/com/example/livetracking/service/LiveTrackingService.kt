package com.example.livetracking.service

import android.app.Application
import android.util.Log
import com.example.livetracking.entity.RideDetail
import com.example.livetracking.entity.Trip
import com.example.livetracking.model.RideDetails
import com.example.livetracking.repository.LiveTrackingRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.types.RealmList
import io.socket.client.Socket
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.mongodb.kbson.BsonObjectId
import java.net.URISyntaxException
import javax.inject.Inject


class LiveTrackingService @Inject constructor(
    val context: Application,
    private val socket: Socket,
    private val json: Json,
    private val realm:Realm
//    private val locationProvider:FusedLocationProviderClient
) : LiveTrackingRepository {
    override suspend fun trackRideDetails(userRideDetails: RideDetails) {
        try {
            Log.d("Hey", "driverLocation: ${json.encodeToString(userRideDetails)}")


            socket.emit("driverLocation", json.encodeToString(userRideDetails))
            socket.on("driverLocation") {
                Log.d("Hey", "trackRideDetails: ${it[0]}")
            }
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    override suspend fun welcome(userRideDetails: RideDetails) {
        try {
            Log.d("Hey", "welcome: ${json.encodeToString(userRideDetails)}")



            val trips=realm.query<Trip>().find()
            val trip:Trip?=trips.query("tripId==$0",userRideDetails.tripId).first().find()
            if(trip==null) {
                realm.write { copyToRealm(Trip().apply {
                    tripId= userRideDetails.tripId
                }) }
            }
            saveToLocal(userRideDetails.toRealmObject(),userRideDetails.tripId)

//            if(userRideDetails.networkAvailable)
                socket.emit("welcome", json.encodeToString(userRideDetails))
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    override suspend fun subscribeToTrip(tripId:String,from:String){
        try {
            val payload="{\"tripId\":\"$tripId\",\"from\":\"$from\"}"

            socket.emit("subscribeToTrip",payload)
        } catch (e:URISyntaxException){
            e.printStackTrace()
        }
    }



    override suspend fun connectSocket() {
        Log.d("Socket Status", "Connected")
        socket.connect()
    }

    override suspend fun disconnectSocket() {
        Log.d("Socket Status", "Disconnected")
        socket.disconnect()
        socket.off()
        socket.close()
//        realm.close()
    }


    private suspend fun saveToLocal(rideDetails: RideDetail,tripId:String){
        val trips=realm.query<Trip>().find()
        val trip:Trip?=trips.query("tripId==$0",tripId).first().find()
        realm.write {
            findLatest(trip!!)?.rideDetails?.add(rideDetails)
        }
    }
}

private fun RideDetails.toRealmObject(): RideDetail {
    return RideDetail().apply {
        lat=this@toRealmObject.lat
        lng=this@toRealmObject.lng
        speed=this@toRealmObject.speed
        accuracy=this@toRealmObject.accuracy
        timestamp=this@toRealmObject.timestamp
        direction=this@toRealmObject.direction
    }
}
