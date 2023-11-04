package com.example.livetracking.di

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.livetracking.AlertActivity
import com.example.livetracking.R
import com.example.livetracking.entity.RideDetail
import com.example.livetracking.entity.Trip
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Configuration
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {
    @Singleton
    @Provides
    fun builder(@ApplicationContext context: Context): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, "100")
            .setSmallIcon(R.drawable.logistics)
            .setContentTitle("Over Speeding Alert")
            .setContentText("Driver is instructed to slow down!!")
            .setPriority(NotificationCompat.PRIORITY_MAX)
    }

}