package com.example.livetracking.worker

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.livetracking.repository.LiveTrackingRepository
import javax.inject.Inject

class LocationWorker @Inject constructor(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getForegroundInfo(): ForegroundInfo {
        Log.d("Hey", "doWork: ${inputData.getString("loc")}")
        return ForegroundInfo(
            123, createNotification()
        )
    }

    override suspend fun doWork(): Result {

        return Result.success()
    }

    private fun createNotification() : Notification {
        var builder = NotificationCompat.Builder(applicationContext, "CHANNEL_ID")
            .setContentTitle("textTitle")
            .setContentText("textContent")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        return builder.build()
    }
}