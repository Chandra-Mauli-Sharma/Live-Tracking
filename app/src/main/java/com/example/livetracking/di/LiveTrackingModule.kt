package com.example.livetracking.di

import com.example.livetracking.repository.LiveTrackingRepository
import com.example.livetracking.service.LiveTrackingService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(ViewModelComponent::class, SingletonComponent::class)
abstract class LiveTrackingModule {
    @Binds
    abstract fun provideLiveTrackingRepository(service:LiveTrackingService): LiveTrackingRepository


}