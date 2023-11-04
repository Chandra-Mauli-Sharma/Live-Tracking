package com.example.livetracking.navigation

import android.content.Context
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.livetracking.util.readString
import com.example.livetracking.view.LiveTrackingScreen
import com.example.livetracking.view.LiveTripList
import com.example.livetracking.view.LoginScreen
import com.example.livetracking.view.TripListScreen
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun LiveTrackingNavHost(
    navHostController: NavHostController,
    locationProviderClient: FusedLocationProviderClient,
    networkRequest: NetworkRequest,
    context: Context
) {

    var startDestination by remember { mutableStateOf(LiveTrackingScreens.LOGIN.name) }
    LaunchedEffect(key1 = startDestination, block = {
        Log.d("heyd", "LiveTrackingNavHost: ${context.readString("driver_id").first()}")
        startDestination = if (context.readString("driver_id").first()
                .isEmpty()
        ) LiveTrackingScreens.LOGIN.name else LiveTrackingScreens.TRIP_LIST.name
    })
    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        builder = {
            composable(
                LiveTrackingScreens.LIVE_TRACK.name + "/{tripId}", arguments = listOf(
                    navArgument("tripId") { type = NavType.StringType },
                )
            ) {
                LiveTrackingScreen(
                    locationProviderClient, networkRequest,
                    it.arguments?.getString("tripId")!!
                )
            }

            composable(LiveTrackingScreens.TRIP_LIST.name) {
                TripListScreen(modifier = Modifier,navHostController)
            }

            composable(LiveTrackingScreens.LOGIN.name) {
                LoginScreen(navHostController)
            }
        })
}