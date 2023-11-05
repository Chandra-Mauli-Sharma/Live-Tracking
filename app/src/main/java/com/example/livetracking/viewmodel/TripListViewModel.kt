package com.example.livetracking.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livetracking.model.Trip
import com.example.livetracking.repository.TripListRepository
import com.example.livetracking.util.readString
import com.example.livetracking.util.writeString
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripListViewModel @Inject constructor(private val tripListRepository: TripListRepository, @ApplicationContext val appContext: Context): ViewModel() {

    private val _tripList=MutableStateFlow<List<Trip>?>(listOf())

    val tripList=_tripList.asStateFlow()

    fun getTripList(){
        _tripList.update {
            emptyList()
        }
        viewModelScope.launch {
            val driverID=appContext.readString("driver_id").first()
            val response=tripListRepository.getTripList(driverID)
            if(response.isSuccessful){
                val body=response.body()

                _tripList.update {
                    body?.data
                }
            }
        }
    }

    fun logout(){
        viewModelScope.launch {
            appContext.writeString("driver_id","")
        }
    }

    fun showNoti(){
        tripListRepository.showNotification()
    }
}