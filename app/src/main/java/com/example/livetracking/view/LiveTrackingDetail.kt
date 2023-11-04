package com.example.livetracking.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.livetracking.model.RideDetails
import com.example.livetracking.viewmodel.LiveTrackingDetailViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import java.util.concurrent.TimeUnit

lateinit var locationCallback: LocationCallback
const val LOCATION_TAG = "Live Location"

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun liveTrackingDetail(
    context: Context,
    connected: Boolean,
    isOnline:Boolean,
    locationProvider: FusedLocationProviderClient,
    tripId:String,
    viewModel: LiveTrackingDetailViewModel = hiltViewModel()
): RideDetails {

    LaunchedEffect(key1 = connected) {
        if (connected) {
            viewModel.connect()
            viewModel.subscribeToTrip(tripId,"driver")
        } else {
            viewModel.disconnect()
        }
    }

    val currentUserRideDetails by viewModel.details.collectAsState()

    val permissionState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
    )
    DisposableEffect(locationProvider) {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                locationProvider.lastLocation
                    .addOnSuccessListener { location ->
                        location?.let {
                            viewModel.onDetailsChanged(
                                RideDetails(
                                    tripId = tripId,
                                    lat = location.latitude,
                                    lng = location.longitude,
                                    speed = location.speed,
                                    accuracy = location.accuracy,
                                    direction = location.bearing,
                                    networkAvailable = isOnline
                                )
                            )
                            Log.d("LiveTest", it.toString())
                        }
                    }
                    .addOnFailureListener {
                        Log.e("Location_error", "${it.message}")
                    }
            }
        }



        if (permissionState.allPermissionsGranted) {
            locationUpdate(context, locationProvider)
        } else {
            permissionState.launchMultiplePermissionRequest()
        }

        onDispose {
//            if (!connected&&isOnline)
                stopLocationUpdate(locationProvider)
        }

    }
    return currentUserRideDetails
}

@SuppressLint("MissingPermission")
fun locationUpdate(context: Context, locationProvider: FusedLocationProviderClient) {
    locationCallback.let {
        val locationRequest =
            LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                TimeUnit.SECONDS.toMillis(15)
            ).setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
//                .setMinUpdateDistanceMeters(5.0f)
                .setIntervalMillis(15000)
                .setMinUpdateIntervalMillis(8000)
                .build()
        Log.d(LOCATION_TAG, "locationUpdate: ${System.currentTimeMillis()}")
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationProvider.requestLocationUpdates(
            locationRequest,
            it,
            Looper.getMainLooper()
        )
    }

}


fun stopLocationUpdate(locationProvider: FusedLocationProviderClient) {
    try {
        //Removes all location updates for the given callback.
        val removeTask = locationProvider.removeLocationUpdates(locationCallback)
        removeTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(LOCATION_TAG, "Location Callback removed.")
            } else {
                Log.d(LOCATION_TAG, "Failed to remove Location Callback.")
            }
        }
    } catch (se: SecurityException) {
        Log.e(LOCATION_TAG, "Failed to remove Location Callback.. $se")
    }
}