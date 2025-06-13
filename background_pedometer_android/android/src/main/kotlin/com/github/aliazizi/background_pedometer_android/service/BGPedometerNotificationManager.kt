package com.github.aliazizi.background_pedometer_android.service

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.github.aliazizi.background_pedometer_android.types.BGPedometerNotificationSettings

class BackgroundPedometerNotificationManager(
    private val context: Context, private val settings: BGPedometerNotificationSettings
) {

    private val notificationManager: NotificationManagerCompat by lazy {
        NotificationManagerCompat.from(context)
    }

    private val notificationBuilder: NotificationCompat.Builder by lazy {
        createNotificationBuilder()
    }


    init {
        initializeNotificationChannel()
    }

    @SuppressLint("MissingPermission")
    fun updateSteps(steps: Int) {
        runCatching {
            notificationManager.notify(
                settings.notificationID,
                notificationBuilder.setContentText(settings.formatMessage(steps)).build()
            )
        }.onFailure {
            Log.e(LOG_TAG, "Failed to update notification", it)
        }
    }

    private fun hasNotificationPermission(): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU || ContextCompat.checkSelfPermission(
            context, Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED

    private fun initializeNotificationChannel() {
        try {
            val channel = NotificationChannelCompat.Builder(
                settings.channelId, NotificationManagerCompat.IMPORTANCE_LOW
            ).apply {
                setName(settings.channelName)
                setDescription(settings.channelDescription)
                setShowBadge(false)
                setLightsEnabled(false)
                setVibrationEnabled(false)
            }.build()

            notificationManager.createNotificationChannel(channel)
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Failed to create notification channel", e)
        }
    }

    private fun createNotificationBuilder(): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, settings.channelId).apply {
            setSmallIcon(settings.resolveIconResId(context))
            setContentTitle(settings.channelName)
            setPriority(NotificationCompat.PRIORITY_LOW)
            setOngoing(true)
        }
    }

    fun getInitialNotification() = notificationBuilder.build()

    companion object {
        private const val LOG_TAG = "PedometerNotification"
    }
}