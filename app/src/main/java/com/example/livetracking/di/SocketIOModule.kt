package com.example.livetracking.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.socket.client.IO
import io.socket.client.Socket
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SocketIOModule {
    @Singleton
    @Provides
    fun provideSocket(): Socket =
        IO.socket("https://4bda-2401-4900-820b-721f-29fc-7536-9d38-d4d3.ngrok-free.app/")
}