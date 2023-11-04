package com.example.livetracking.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livetracking.model.RideDetails
import com.example.livetracking.repository.LiveTrackingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LiveTrackingDetailViewModel @Inject constructor(
    private val repository: LiveTrackingRepository
) : ViewModel() {
    @RequiresApi(Build.VERSION_CODES.O)
    private val _details: MutableStateFlow<RideDetails> = MutableStateFlow(RideDetails())
    @RequiresApi(Build.VERSION_CODES.O)
    val details = _details.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun addRideDetails() {
        viewModelScope.launch {
            details.collect {
                repository.trackRideDetails(it)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun welcome() {
        viewModelScope.launch {
            details.collect {
                repository.welcome(it)
            }
        }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun onDetailsChanged(details: RideDetails) {
        viewModelScope.launch(Dispatchers.Main) {
            _details.update {
//                delay(5000)
                details
            }
            repository.trackRideDetails(details)
        }
    }

    fun connect() {
        viewModelScope.launch {
            repository.connectSocket()
        }
    }

    fun subscribeToTrip(tripId:String,from:String){
        viewModelScope.launch {
            repository.subscribeToTrip(tripId,from)
        }
    }

    fun disconnect() {
        viewModelScope.launch {
            repository.disconnectSocket()
        }
    }

    fun showNoti(){
        repository.showNotification()
    }
}