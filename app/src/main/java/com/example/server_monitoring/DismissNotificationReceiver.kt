package com.example.server_monitoring

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class DismissNotificationReceiver<Intent> : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: android.content.Intent?) {
        if (p1?.action == "dismiss_notification") {
            // Handle dismiss action here
            val notificationManager = p0?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(1) // Cancel the notification with id 1
        }
    }

}