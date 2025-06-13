package com.github.aliazizi.background_pedometer_android.processing

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener

class BGPedometerSensorListener(
    private val processor: BGPedometerSensorProcessor, private val callback: (Int) -> Unit
) : SensorEventListener {
    override fun onSensorChanged(event: SensorEvent?) {
        TODO("Not yet implemented")
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        TODO("Not yet implemented")
    }
}