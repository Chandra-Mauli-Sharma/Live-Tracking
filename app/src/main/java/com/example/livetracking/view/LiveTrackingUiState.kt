package com.example.livetracking.view

sealed class LiveTrackingUiState {
    object OfflineAndConnected:LiveTrackingUiState()
    object OfflineAndDisconnected:LiveTrackingUiState()
    object OnlineAndDisconnected:LiveTrackingUiState()
    object OnlineAndConnected:LiveTrackingUiState()
    object Connected:LiveTrackingUiState()
    object Disconnected:LiveTrackingUiState()
}