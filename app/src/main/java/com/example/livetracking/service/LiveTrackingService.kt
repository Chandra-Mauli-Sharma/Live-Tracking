package com.example.livetracking.service

import android.Manifest
import android.app.Application
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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
    private val realm:Realm,
    private val builder: NotificationCompat.Builder
//    private val locationProvider:FusedLocationProviderClient
) : LiveTrackingRepository {
    override suspend fun trackRideDetails(userRideDetails: RideDetails) {
        try {
            Log.d("Hey", "driverLocation: ${json.encodeToString(userRideDetails)}")

            if(userRideDetails.speed>23)
                showNotification()

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

    override fun showNotification(){
        with(NotificationManagerCompat.from(context)){
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notify(100,builder.build())
        }
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
