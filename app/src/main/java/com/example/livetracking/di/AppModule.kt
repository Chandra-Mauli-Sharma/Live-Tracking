package com.example.livetracking.di

import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.example.livetracking.MainActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideMainActivity(): MainActivity = MainActivity.getInstance() as MainActivity

    @Singleton
    @Provides
    fun provideJson(): Json = Json {
        encodeDefaults=true
    }

    @Singleton
    @Provides
    fun provideNetworkRequest():NetworkRequest= NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()
}