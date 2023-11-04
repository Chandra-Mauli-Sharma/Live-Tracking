package com.example.livetracking.di

import android.content.Context
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {
//    @Singleton
//    @Provides
//    fun locationRequest(): LocationRequest =
//        LocationRequest.Builder(
//            Priority.PRIORITY_HIGH_ACCURACY,
//            TimeUnit.SECONDS.toMillis(10)
//        ).setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
//            .setMinUpdateDistanceMeters(5.0f)
//            .setIntervalMillis(10000)
//            .setMinUpdateIntervalMillis(5000)
//            .build()
//

    @Singleton
    @Provides
    fun provideLocationProvider(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
}
