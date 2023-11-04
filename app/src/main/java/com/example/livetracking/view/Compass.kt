package com.example.livetracking.view

import android.hardware.SensorManager
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.mutualmobile.composesensors.rememberAccelerometerSensorState
import com.mutualmobile.composesensors.rememberMagneticFieldSensorState
import kotlinx.coroutines.runBlocking

@Composable
fun compass(): Double {
    val magentometer = rememberMagneticFieldSensorState()
    val accelerometer = rememberAccelerometerSensorState()

    val rotationMatrix = FloatArray(9)
    val orientation = FloatArray(3)
    val accelerometerResult =
        floatArrayOf(accelerometer.xForce, accelerometer.yForce, accelerometer.zForce)

    val magentometerResult =
        floatArrayOf(magentometer.xStrength, magentometer.yStrength, magentometer.zStrength)


    SensorManager.getRotationMatrix(
        rotationMatrix,
        null,
        accelerometerResult,
        magentometerResult
    )

    SensorManager.getOrientation(rotationMatrix, orientation)

    return ((Math.toDegrees(orientation[0].toDouble()) + 360) % 360)
}