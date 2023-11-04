package com.example.livetracking.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livetracking.repository.LiveTrackingRepository
import com.example.livetracking.view.LiveTrackingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LiveTrackingViewModel @Inject constructor(private val repository: LiveTrackingRepository): ViewModel() {
    private val _uiState:MutableStateFlow<LiveTrackingUiState> = MutableStateFlow(LiveTrackingUiState.Disconnected)
    val uiState=_uiState.asStateFlow()

    fun onUiStateChanged(uiState: LiveTrackingUiState){
        _uiState.update {
            uiState
        }
    }


    fun showNoti(){
        repository.showNotification()
    }
}