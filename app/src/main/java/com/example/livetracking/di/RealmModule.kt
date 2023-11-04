package com.example.livetracking.di

import com.example.livetracking.entity.RideDetail
import com.example.livetracking.entity.Trip
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Configuration
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RealmModule {

    @Singleton
    @Provides
    fun config():Configuration= RealmConfiguration.Builder(schema = setOf(RideDetail::class,Trip::class)).deleteRealmIfMigrationNeeded().build()

    @Singleton
    @Provides
    fun realm(configuration: Configuration):Realm=Realm.open(configuration)
}