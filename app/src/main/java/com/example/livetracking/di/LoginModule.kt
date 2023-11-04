package com.example.livetracking.di

import com.example.livetracking.repository.LiveTrackingRepository
import com.example.livetracking.repository.LoginRepository
import com.example.livetracking.service.LiveTrackingService
import com.example.livetracking.service.LoginService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(ViewModelComponent::class, SingletonComponent::class)
abstract class LoginModule {
    @Binds
    abstract fun provideLoginRepository(service: LoginService): LoginRepository


}