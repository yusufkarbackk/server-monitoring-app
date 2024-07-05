package com.example.server_monitoring

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.POST

class SensorService : Service() {
    val url = "https://server-monitoring-2-l7uklkz22a-et.a.run.app/"
    private lateinit var requestQueue: RequestQueue
    private lateinit var apiService: ApiService
    private val channelId = "sensor_alert_channel"
    private val channelName = "Sensor Alert Channel"
    private lateinit var database: DatabaseReference
    private lateinit var datalistener: ValueEventListener
    override fun onCreate() {
        super.onCreate()
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        database = Firebase.database.getReference("sensorData")
        apiService = retrofit.create(ApiService::class.java)

        datalistener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val latestData = snapshot.children.last()
                    val kelembaban =
                        latestData.child("kelembaban").getValue(String::class.java) ?: 0.0
                    val suhu = latestData.child("suhu").getValue(String::class.java) ?: 0.0
                    val teganganAC =
                        latestData.child("teganganAC").getValue(String::class.java) ?: 0.0

                    Log.d("SensorService", "Data retrieved: suhu=$suhu, kelembaban=$kelembaban, teganganAC=$teganganAC")

                    val sensorDataRequest = SensorDataRequest(suhu.toString(),
                        kelembaban.toString(), teganganAC.toString()
                    )

                    sendSensorData(sensorDataRequest)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        }
        database.addValueEventListener(datalistener)
        startForegroundService()
    }

    private fun sendSensorData(sensorDataRequest: SensorDataRequest) {
        // Make network call on a background thread
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.sendSensorData(sensorDataRequest)
                Log.d("SensorService", "Response: $response")

                val intent = Intent("com.example.server_monitoring.SENSOR_DATA")
                intent.putExtra("suhu", response.decryptedSuhu)
                intent.putExtra("kelembaban", response.decryptedKelembaban)
                intent.putExtra("teganganAC", response.decryptedTeganganAC)
                sendBroadcast(intent)

                val rawSensorData = SensorData(response.decryptedTeganganAC, response.decryptedSuhu, response.decryptedTeganganAC)
                val fuzzifiedData = FuzzyLogic().fuzzify(rawSensorData)

                val aboveThresholdSensors = FuzzyLogic().getAboveThresholdSensors(fuzzifiedData)
                val belowThresholdSensors = FuzzyLogic().getBelowThresholdSensors(fuzzifiedData)

                val sensorData = SensorData(response.decryptedKelembaban, response.decryptedSuhu, response.decryptedTeganganAC)
                Log.d("SensorService", "Response: $aboveThresholdSensors")
                Log.d("SensorService", "Response: $belowThresholdSensors")


                if (aboveThresholdSensors.isNotEmpty()){
                    aboveThresholdSensors.forEach { sensor ->
                        sendNotification("$sensor value exceeds threshold: ${FuzzyLogic().getSensorValue(sensor, sensorData)}")
                    }
                }

                if (belowThresholdSensors.isNotEmpty()){
                    belowThresholdSensors.forEach { sensor ->
                        sendNotification("$sensor value is below threshold: ${FuzzyLogic().getSensorValue(sensor, sensorData)}")
                    }
                }


            } catch (e: Exception) {
                Log.e("SensorService", "Network error: ${e.message}", e)
            }
        }
    }

    @SuppressLint("ForegroundServiceType")
    private fun startForegroundService() {
        val channelId = "sensor_service_channel"
        val channelName = "Sensor Service Channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Server Room")
            .setContentText("Monitoring Status: Active")
            .setSmallIcon(R.drawable.logo)
            .build()
        startForeground(1, notification)
    }

    fun checkAndNotify(sensorValue: Double, context: Context) {}

    fun sendNotification(value: String) {

        // Create an explicit intent for launching WhatsApp
        val whatsappIntent = Intent(Intent.ACTION_VIEW)
        whatsappIntent.data = Uri.parse("https://wa.me/+62895372461140") // Replace <phone_number> with actual number
        whatsappIntent.setPackage("com.whatsapp")

        // Create a PendingIntent for WhatsApp action
        val whatsappPendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            whatsappIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Create an intent for dismissing the notification
        val dismissIntent = Intent(applicationContext, DismissNotificationReceiver::class.java)
        dismissIntent.action = "dismiss_notification"

        // Create a PendingIntent for dismiss action
        val dismissPendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            0,
            dismissIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel =
                    NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
                val notificationManager = getSystemService(NotificationManager::class.java)
                notificationManager.createNotificationChannel(channel)
            }
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "An error occurred: ${e.message}")
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Sensor Alert")
            .setContentText("Sensor value exceeds threshold: $value")
            .setSmallIcon(R.drawable.logo)
            .addAction(R.drawable.logo, "Call via Whahtsapp", whatsappPendingIntent)
            .addAction(R.drawable.logo, "Dismiss", dismissPendingIntent)
            .setAutoCancel(true)
        notificationManager.notify(0, notificationBuilder.build())
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


}