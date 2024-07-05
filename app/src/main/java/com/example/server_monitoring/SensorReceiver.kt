package com.example.server_monitoring

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class SensorReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val sensorValue = intent.getDoubleExtra("sensorValue", 0.0)
        val serviceIntent = Intent(context, SensorService::class.java).apply {
            putExtra("sensorValue", sensorValue)
        }
        context.startService(serviceIntent)
    }

}