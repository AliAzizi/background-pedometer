package com.github.aliazizi.background_pedometer_android.types

import java.time.Instant

class BGPedometerData private constructor(
    private val numberOfSteps: UInt, private val startUtc: Instant, private val endUtc: Instant
) {
    init {
        require(!startUtc.isAfter(endUtc)) { "Start time must not be after end time" }
    }

    fun toMapForDart(): Map<String, Any> = mapOf(
        "numberOfSteps" to numberOfSteps,
        "start" to startUtc.toEpochMilli(),
        "end" to endUtc.toEpochMilli()
    )
}
