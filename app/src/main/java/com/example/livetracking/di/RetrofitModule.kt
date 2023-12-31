package com.example.livetracking.di

import com.example.livetracking.retrofit.LoginInterface
import com.example.livetracking.retrofit.TripListInterface
import com.example.livetracking.util.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ViewModelComponent::class, SingletonComponent::class)
object RetrofitModule {

    @Provides
    fun provideGson(): Gson? {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Provides
    fun provideRetrofit(client: OkHttpClient?, gson: Gson?): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.baseURL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client!!)
            .build()
    }


    @Provides
    fun provideLoginRepository(retrofit: Retrofit): LoginInterface {
        return retrofit.create(LoginInterface::class.java)
    }

    @Provides
    fun provideTripListRepository(retrofit: Retrofit):TripListInterface{
        return retrofit.create(TripListInterface::class.java)
    }
}