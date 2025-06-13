package com.github.aliazizi.background_pedometer_android.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.content.IntentCompat
import com.github.aliazizi.background_pedometer_android.BGPedometerConstants
import com.github.aliazizi.background_pedometer_android.types.BGPedometerSettings
import com.github.aliazizi.background_pedometer_android.types.BGServiceStartOrigin

class BGPedometerService : Service() {

    private val binder = LocalBinder()


    private var currentSettings: BGPedometerSettings? = null

    private val notificationManager by lazy {
        BackgroundPedometerNotificationManager(
            context = this,
            currentSettings?.notification ?: throw IllegalStateException("Settings not initialized")
        )
    }

    companion object {
        private const val TAG = "BGPedometerService"

        fun createStartIntent(
            context: Context, settings: BGPedometerSettings, origin: BGServiceStartOrigin
        ) = Intent(context, BGPedometerService::class.java).apply {
            action = BGPedometerConstants.ACTION_START_PEDOMETER_SERVICE
            putExtra(BGPedometerConstants.EXTRA_BG_SETTINGS, settings)
            putExtra(BGPedometerConstants.EXTRA_BG_SERVICE_START_ORIGIN, origin.name)
        }

    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.action?.let { action ->
            when (action) {
                BGPedometerConstants.ACTION_START_PEDOMETER_SERVICE -> handleStartAction(intent)
                else -> Log.w(TAG, "Unhandled action: $action")
            }
        } ?: Log.w(TAG, "Received null intent or action")

        return START_STICKY
    }


    private fun handleStartAction(intent: Intent) {
        val settings = IntentCompat.getParcelableExtra(
            intent, BGPedometerConstants.EXTRA_BG_SETTINGS, BGPedometerSettings::class.java
        )
        val originName = intent.getStringExtra(BGPedometerConstants.EXTRA_BG_SERVICE_START_ORIGIN)


        if (settings == null || originName == null) {
            Log.e(TAG, "Invalid start intent: missing settings or origin")
            stopSelf()
            return
        }

        startForeground(
            settings.notification.notificationID, notificationManager.getInitialNotification()
        )
    }


    override fun onBind(intent: Intent?): IBinder = binder

    inner class LocalBinder : Binder() {
        fun getService(): BGPedometerService = this@BGPedometerService
    }

}