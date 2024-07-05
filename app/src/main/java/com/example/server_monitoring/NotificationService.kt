package com.example.server_monitoring

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService

class NotificationService: Service() {


    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "server monitoring"
            val descriptionText = "monitoring notification"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("ServerMonitoringTMJ", name, importance).apply {
                descriptionText
            }

            val notificationManager: NotificationManager = getSystemService(
                context,
                NotificationManager::class.java
            ) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun sendNotification(sensorValue: Double, context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "sensor_alert_channel"
        val channelName = "Sensor Alert Channel"

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
                notificationManager.createNotificationChannel(channel)
            }

            val notificationBuilder = NotificationCompat.Builder(context, channelId)
                .setContentTitle("Anomaly Detected")
                .setContentText("Sensor value exceeded threshold: $sensorValue")
                .setSmallIcon(R.drawable.logo)
                .setAutoCancel(true)

            notificationManager.notify(0, notificationBuilder.build())
        } catch(e: Exception){
            Log.e(TAG, "An error occurred: ${e.message}")
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}
