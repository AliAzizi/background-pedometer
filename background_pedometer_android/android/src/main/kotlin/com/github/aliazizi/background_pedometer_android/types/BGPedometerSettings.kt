package com.github.aliazizi.background_pedometer_android.types

import android.content.Context
import android.content.SharedPreferences
import android.hardware.Sensor
import android.os.Parcel
import android.os.Parcelable
import android.util.Base64
import com.github.aliazizi.background_pedometer_android.exceptions.MissingNotificationDrawableException
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

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


class BGPedometerSettingsDelegate(
    private val prefs: SharedPreferences
) : ReadWriteProperty<Any?, BGPedometerSettings?> {

    override fun getValue(thisRef: Any?, property: KProperty<*>): BGPedometerSettings? {
        if (!prefs.contains("autoRestart")) {
            return null
        }

        val sensorFallbackOrder = prefs.getString("sensorFallbackOrder", "")?.split(",")
            ?.mapNotNull { BGPedometerSensorType.byName(it) } ?: return null

        val hasNotification = prefs.getBoolean("notif_enabled", false)
        val notification = if (hasNotification) {
            val channelId = prefs.getString("notif_channelId", null) ?: return null
            val channelName = prefs.getString("notif_channelName", null) ?: return null
            val icon = prefs.getString("notif_icon", null) ?: return null
            val messageTemplate = prefs.getString("notif_messageTemplate", null) ?: return null
            val notificationID = prefs.getInt("notif_notificationID", -1)
            if (notificationID == -1) return null

            BGPedometerNotificationSettings(
                channelId = channelId,
                channelName = channelName,
                icon = icon,
                messageTemplate = messageTemplate,
                notificationID = notificationID,
                channelDescription = prefs.getString("notif_channelDescription", null)
            )
        } else null

        return BGPedometerSettings(
            autoRestart = prefs.getBoolean("autoRestart", false),
            autoRunOnBoot = prefs.getBoolean("autoRunOnBoot", false),
            autoRunOnMyPackageReplaced = prefs.getBoolean("autoRunOnMyPackageReplaced", false),
            shutdownAware = prefs.getBoolean("shutdownAware", false),
            sensorFallbackOrder = sensorFallbackOrder,
            notification = notification
        )
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: BGPedometerSettings?) {

        prefs.edit().apply {
            if (value == null) {
                clear()
            } else {
                putBoolean("autoRestart", value.autoRestart)
                putBoolean("autoRunOnBoot", value.autoRunOnBoot)
                putBoolean("autoRunOnMyPackageReplaced", value.autoRunOnMyPackageReplaced)
                putBoolean("shutdownAware", value.shutdownAware)
                putString("sensorFallbackOrder", value.sensorFallbackOrder.joinToString(","))

                value.notification?.let {
                    putString("notif_channelId", it.channelId)
                    putString("notif_channelName", it.channelName)
                    putString("notif_icon", it.icon)
                    putString("notif_messageTemplate", it.messageTemplate)
                    putInt("notif_notificationID", it.notificationID)
                    putString("notif_channelDescription", it.channelDescription)
                    putBoolean("notif_enabled", true)
                } ?: putBoolean("notif_enabled", false)
            }
            apply()
        }
    }
}
