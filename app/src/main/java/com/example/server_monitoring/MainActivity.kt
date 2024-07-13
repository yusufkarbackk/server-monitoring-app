package com.example.server_monitoring

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //NotificationService().createNotificationChannel(this)
        var auth: FirebaseAuth = Firebase.auth
        startAppService()
        if (auth.currentUser != null){
            val intentSuhu = Intent(this, suhu::class.java)
            Log.d("activity", "start activity suhu screen")
            startActivity(intentSuhu)
        } else{
            val intentLogin = Intent(this, LoginScreen::class.java)
            Log.d("activity", "start activity login screen")
            startActivity(intentLogin)
        }
    }
    
    fun startAppService(){
        val serviceIntent = Intent(this, SensorService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            Log.d("foreground service", "start foreground service SensorService")
            startForegroundService(serviceIntent)
        } else{
            Log.d("foreground service", "start foreground service SensorService")
            startService(serviceIntent)
        }
    }
}


