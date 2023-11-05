package com.example.livetracking.view

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.livetracking.model.RideDetails
import com.example.livetracking.viewmodel.LiveTrackingViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun LiveTrackingScreen(
    locationProvider: FusedLocationProviderClient,
    networkRequest: NetworkRequest,
    tripId:String,
    viewModel: LiveTrackingViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val connectivityManager = ContextCompat.getSystemService(
        context,
        ConnectivityManager::class.java
    ) as ConnectivityManager

    val uiState by viewModel.uiState.collectAsState()


    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        return@rememberPagerState 2
    }

    LaunchedEffect(key1 = Unit, block = {

        viewModel.showNoti()
    })

    LaunchedEffect(key1 = connectivityManager) {

        val networkCallback = object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                super.onAvailable(network)

                Log.d("Network Test", "Available")

                when (uiState) {
                    is LiveTrackingUiState.Connected ->
                        viewModel.onUiStateChanged(LiveTrackingUiState.OnlineAndConnected)

                    is LiveTrackingUiState.OfflineAndConnected -> viewModel.onUiStateChanged(
                        LiveTrackingUiState.OnlineAndConnected
                    )

                    is LiveTrackingUiState.Disconnected -> viewModel.onUiStateChanged(
                        LiveTrackingUiState.OnlineAndDisconnected
                    )

                    else -> {

                    }
                }
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)
            }

            override fun onLost(network: Network) {
                super.onLost(network)

                Log.d("Network Test", "Lost")

                when (uiState) {
                    is LiveTrackingUiState.Connected -> viewModel.onUiStateChanged(
                        LiveTrackingUiState.OfflineAndConnected
                    )

                    is LiveTrackingUiState.Disconnected -> viewModel.onUiStateChanged(
                        LiveTrackingUiState.OfflineAndDisconnected
                    )

                    is LiveTrackingUiState.OnlineAndConnected -> viewModel.onUiStateChanged(
                        LiveTrackingUiState.OfflineAndConnected
                    )

                    else -> {

                    }
                }
            }
        }

        connectivityManager.requestNetwork(networkRequest, networkCallback)

    }

    val coroutine = rememberCoroutineScope()
    Scaffold {
        Column(modifier = Modifier.padding(it)) {
            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AnimatedVisibility(
                    !((uiState == LiveTrackingUiState.Connected) || (uiState == LiveTrackingUiState.OnlineAndConnected)),
                    enter = slideInHorizontally(),
                    exit = slideOutHorizontally()
                ) {
                    Text(
                        text = "Start Trip",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                }

                if ((uiState == LiveTrackingUiState.Connected) || (uiState == LiveTrackingUiState.OnlineAndConnected))
                    Button(onClick = {
                        coroutine.launch {
                            if (pagerState.currentPage == 0) {
                                pagerState.animateScrollToPage(1)
                            } else {
                                pagerState.animateScrollToPage(0)
                            }
                        }
                    }) {
                        if (pagerState.currentPage == 0) {
                            Text(text = "Watch Stats")
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowRight,
                                contentDescription = null
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowLeft,
                                contentDescription = null
                            )
                            Text(text = "Watch Map")
                        }
                    }

                Switch(
                    checked = (uiState is LiveTrackingUiState.Connected) || (uiState is LiveTrackingUiState.OnlineAndConnected),
                    onCheckedChange = { checked ->
                        viewModel.onUiStateChanged(if (checked) LiveTrackingUiState.OnlineAndConnected else LiveTrackingUiState.Disconnected)
                    })
            }

            AnimatedVisibility(
                visible = !((uiState is LiveTrackingUiState.Connected) || (uiState is LiveTrackingUiState.OnlineAndConnected)),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 30.dp)
                ) {
                    Text(text = "Start the ride")
                    Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = null)
                }
            }

            AnimatedVisibility(visible = uiState is LiveTrackingUiState.OnlineAndConnected || uiState is LiveTrackingUiState.OfflineAndConnected) {
                with(
                    liveTrackingDetail(
                        context = context,
                        connected = uiState is LiveTrackingUiState.OnlineAndConnected || uiState is LiveTrackingUiState.OfflineAndConnected,
                        isOnline = uiState is LiveTrackingUiState.OnlineAndConnected,
                        locationProvider = locationProvider,
                        tripId = tripId
                    )
                ) {
                    HorizontalPager(state = pagerState, userScrollEnabled = false) {
                        when (it) {
                            1 -> RideDetails(this@with)
                            0 -> RideMap(this@with)
                        }
                    }

                }
            }
//            when(uiState){
//                 is LiveTrackingUiState.OnlineAndConnected->
//                    AnimatedVisibility(visible = uiState is LiveTrackingUiState.OnlineAndConnected) {
//                        with(
//                            liveTrackingDetail(
//                                context = context,
//                                connected = true,
//                                isOnline = true,
//                                locationProvider = locationProvider
//                            )
//                        ) {
//                            HorizontalPager(state = pagerState, userScrollEnabled = false) {
//                                when (it) {
//                                    1 -> RideDetails(this@with)
//                                    0 -> RideMap()
//                                }
//                            }
//
//                        }
//                    }
//                 is LiveTrackingUiState.OfflineAndConnected ->
//                    AnimatedVisibility(visible = uiState is LiveTrackingUiState.OfflineAndConnected) {
//                        with(
//                            liveTrackingDetail(
//                                context = context,
//                                connected = uiState is LiveTrackingUiState.OfflineAndConnected,
//                                isOnline = false,
//                                locationProvider = locationProvider
//                            )
//                        ) {
//                            HorizontalPager(state = pagerState, userScrollEnabled = false) {
//                                when (it) {
//                                    1 -> RideDetails(this@with)
//                                    0 -> RideMap()
//                                }
//                            }
//
//                        }
//                    }
//                else -> {}
//            }
        }
    }
}

@Composable
fun RideDetails(rideDetails: RideDetails) {
    LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.fillMaxSize()) {
        item {
            StatsCard(fieldName = "Lat", fieldValue = rideDetails.lat.toString())
        }

        item {
            StatsCard(fieldName = "Long", fieldValue = rideDetails.lng.toString())
        }

        item {
            StatsCard(fieldName = "Speed", fieldValue = rideDetails.speed.toString())
        }

        item {
            StatsCard(fieldName = "Direction", fieldValue = rideDetails.direction.toString())
        }

        item {
            StatsCard(fieldName = "Accuracy", fieldValue = rideDetails.accuracy.toString())
        }
    }
}


@Composable
fun RideMap(rideDetails: RideDetails) {
//    var cameraPositionState=rememberCameraPositionState()
    GoogleMap(cameraPositionState = CameraPositionState(CameraPosition(LatLng(rideDetails.lat,rideDetails.lng),20f,0.0f,rideDetails.direction)),
        uiSettings = MapUiSettings(),
        properties =    MapProperties(
            mapType = MapType.NORMAL,
            isIndoorEnabled = true,
            isBuildingEnabled = true,
            isMyLocationEnabled = true,
        ),
        modifier = Modifier
            .padding(20.dp)
            .clip(RoundedCornerShape(10.dp))
    ) {

    }
}

@Composable
fun LazyGridItemScope.StatsCard(fieldName: String, fieldValue: String) {
    Card(modifier = Modifier.padding(10.dp)) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = fieldName,
                style = MaterialTheme.typography.headlineMedium
            )
            Text(text = fieldValue)
        }
    }
}
