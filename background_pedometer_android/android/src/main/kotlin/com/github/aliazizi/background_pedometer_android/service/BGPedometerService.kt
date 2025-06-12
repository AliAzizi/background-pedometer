package com.github.aliazizi.background_pedometer_android.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.github.aliazizi.background_pedometer_android.BGPedometerConstants
import com.github.aliazizi.background_pedometer_android.types.BGPedometerSettings
import com.github.aliazizi.background_pedometer_android.types.BGServiceStartOrigin

class BGPedometerService : Service() {

    private val binder = LocalBinder()

    companion object {
        private const val TAG = "BGPedometerService"

        fun createStartIntent(context: Context, settings: BGPedometerSettings, origin: BGServiceStartOrigin): Intent {
            return Intent(context, BGPedometerSettings::class.java).apply {
                action = BGPedometerConstants.ACTION_START_PEDOMETER_SERVICE
                putExtra(BGPedometerConstants.EXTRA_BG_SETTINGS, settings)
                putExtra(BGPedometerConstants.EXTRA_BG_SERVICE_START_ORIGIN, origin.name)
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    inner class LocalBinder : Binder() {
        fun getService(): BGPedometerService = this@BGPedometerService
    }

}