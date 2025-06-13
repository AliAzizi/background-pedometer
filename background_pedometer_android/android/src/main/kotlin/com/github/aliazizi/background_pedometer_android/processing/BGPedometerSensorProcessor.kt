package com.github.aliazizi.background_pedometer_android.processing

import android.hardware.SensorEvent

interface BGPedometerSensorProcessor {
    fun process(event: SensorEvent): Int
}