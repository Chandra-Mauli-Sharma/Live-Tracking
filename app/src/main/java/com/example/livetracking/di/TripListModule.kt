package com.example.livetracking.di

import com.example.livetracking.repository.LiveTrackingRepository
import com.example.livetracking.repository.TripListRepository
import com.example.livetracking.service.LiveTrackingService
import com.example.livetracking.service.TripListService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(ViewModelComponent::class, SingletonComponent::class)
abstract class TripListModule {
    @Binds
    abstract fun provideTripListRepository(service: TripListService): TripListRepository


}