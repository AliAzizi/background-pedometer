package com.github.aliazizi.background_pedometer_android.types

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.os.Parcelable
import com.github.aliazizi.background_pedometer_android.exceptions.MissingNotificationDrawableException
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class BGPedometerSettings(
    val autoRestart: Boolean,
    val autoRunOnBoot: Boolean,
    val autoRunOnMyPackageReplaced: Boolean,
    val shutdownAware: Boolean,
    val sensorFallbackOrder: List<BGPedometerSensorType>,
    val notification: BGPedometerNotificationSettings?
) : Parcelable {

    companion object {
        @JvmStatic
        fun fromMap(map: Map<String, Any?>): BGPedometerSettings {
            val sensorFallbackOrder = (map["sensorFallbackOrder"] as List<*>).mapNotNull {
                BGPedometerSensorType byName it as String
            }

            val notification =
                (map["notification"] as? Map<*, *>)?.let(BGPedometerNotificationSettings::fromMap)

            return BGPedometerSettings(
                autoRestart = map["autoRestart"] as Boolean,
                autoRunOnBoot = map["autoRunOnBoot"] as Boolean,
                autoRunOnMyPackageReplaced = map["autoRunOnMyPackageReplaced"] as Boolean,
                shutdownAware = map["shutdownAware"] as Boolean,
                sensorFallbackOrder = sensorFallbackOrder,
                notification = notification
            )
        }
    }
}

@Parcelize
data class BGPedometerNotificationSettings(
    val channelId: String,
    val channelName: String,
    val icon: String,
    val messageTemplate: String,
    val notificationID: Int,
    val channelDescription: String?
) : Parcelable {

    @IgnoredOnParcel
    private var _resolvedIconResId: Int? = null

    fun resolveIconResId(context: Context): Int {
        return _resolvedIconResId ?: run {
            val resId = context.resources.getIdentifier(icon, "drawable", context.packageName)
                .takeIf { it != 0 } ?: throw MissingNotificationDrawableException(icon)

            _resolvedIconResId = resId
            resId
        }
    }

    companion object {
        @JvmStatic
        infix fun fromMap(map: Map<*, *>): BGPedometerNotificationSettings {
            return BGPedometerNotificationSettings(
                channelId = map["channelId"] as String,
                channelName = map["channelName"] as String,
                icon = map["icon"] as String,
                messageTemplate = map["messageTemplate"] as String,
                notificationID = (map["notificationID"] as Number).toInt(),
                channelDescription = map["channelDescription"] as? String
            )
        }
    }
}

enum class BGPedometerSensorType(val sensorTypeId: Int) {
    STEP_COUNTER(Sensor.TYPE_STEP_COUNTER);

    companion object {
        private val nameToTypeMap by lazy {
            entries.associateBy { it.name.uppercase() }
        }

        infix fun byName(name: String) = nameToTypeMap[name.uppercase()]
    }
}