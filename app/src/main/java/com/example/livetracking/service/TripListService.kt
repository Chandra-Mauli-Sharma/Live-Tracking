package com.example.livetracking.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.livetracking.model.TripListResponse
import com.example.livetracking.repository.TripListRepository
import com.example.livetracking.retrofit.TripListInterface
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Response
import javax.inject.Inject

class TripListService @Inject constructor(
    private val tripListInterface: TripListInterface,
    @ApplicationContext private val context: Context,
    private val builder: NotificationCompat.Builder
) : TripListRepository {
    override suspend fun getTripList(driverID: String): Response<TripListResponse> {
        return tripListInterface.getTripList(mapOf(Pair("driverId", driverID.toLong())))
    }


    override fun showNotification() {
        createNotificationChannel()
        with(NotificationManagerCompat.from(context)) {
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
            notify(100, builder.build())
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Test"
            val descriptionText = "desc"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("100", name, importance).apply {
                description = descriptionText

            }
            // Register the channel with the system.
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
