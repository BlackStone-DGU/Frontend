package com.example.blackstone.pedometer

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlin.math.max

class PedometerManager(
    private val context: Context,
    private val strideMeters: Double = 0.7 // 기본 보폭(m).
) : DefaultLifecycleObserver, SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var stepCounter: Sensor? = null

    private var sessionStartSteps: Float? = null
    private var lastEmittedSteps: Int = 0


    var onDistanceUpdate: ((km: Double, steps: Int) -> Unit)? = null

    override fun onStart(owner: LifecycleOwner) {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepCounter = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        stepCounter?.let {
            sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        sensorManager?.unregisterListener(this)
        sensorManager = null
        stepCounter = null
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type != Sensor.TYPE_STEP_COUNTER) return
        val totalDeviceSteps = event.values.firstOrNull() ?: return

        if (sessionStartSteps == null) {
            sessionStartSteps = totalDeviceSteps
            lastEmittedSteps = 0
            onDistanceUpdate?.invoke(0.0, 0)
            return
        }

        val stepsDelta = max(0f, totalDeviceSteps - (sessionStartSteps ?: 0f)).toInt()
        if (stepsDelta != lastEmittedSteps) {
            lastEmittedSteps = stepsDelta
            val distanceMeters = stepsDelta * strideMeters
            val distanceKm = distanceMeters / 1000.0
            onDistanceUpdate?.invoke(distanceKm, stepsDelta)
        }
    }
}