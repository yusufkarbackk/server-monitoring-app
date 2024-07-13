package com.example.server_monitoring

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.volley.RequestQueue
import com.example.server_monitoring.ui.theme.ProjektmjTheme
import com.example.server_monitoring.viewModel.MainViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class suhu : ComponentActivity() {
    private lateinit var sensorDataReceiver: BroadcastReceiver
    private lateinit var filter: IntentFilter

    val url = "https://server-monitoring-2-l7uklkz22a-et.a.run.app/"
    private lateinit var apiService: ApiService
    // A surface container using the 'background' color from the theme
    var state_kelembaban by mutableStateOf<Double?>(null)
    var state_suhu by mutableStateOf<Double?>(null)
    var state_teganganAC by  mutableStateOf<Double?>(null)
    var sensorData by  mutableStateOf<SensorData?>(null)
    var isLoading by mutableStateOf(true)
    val database = Firebase.database.getReference("sensorData")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)
        super.onCreate(savedInstanceState)
        setContent {
            //broadcastReceiver
            sensorDataReceiver = object : BroadcastReceiver() {
                override fun onReceive(p0: Context?, p1: Intent?) {
                    if (p1 != null) {
                        state_kelembaban = p1.getDoubleExtra("kelembaban", 0.0)
                    }
                    if (p1 != null) {
                        state_suhu = p1.getDoubleExtra("suhu", 0.0)
                    }
                    if (p1 != null) {
                        state_teganganAC = p1.getDoubleExtra("teganganAC", 0.0)
                    }

                    sensorData = SensorData(state_kelembaban, state_suhu, state_teganganAC)
                    isLoading = false
                }
            }

            val filter = IntentFilter("com.example.server_monitoring.SENSOR_DATA")
            registerReceiver(sensorDataReceiver, filter, RECEIVER_NOT_EXPORTED)

//            LaunchedEffect(Unit) {
//                val dataListener = object : ValueEventListener {
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        if (snapshot.exists()) {
//                            val latestData = snapshot.children.last()
//                            val kelembaban =
//                                latestData.child("kelembaban").getValue(String::class.java) ?: 0.0
//                            val suhu = latestData.child("suhu").getValue(String::class.java) ?: 0.0
//                            val teganganAC =
//                                latestData.child("teganganAC").getValue(String::class.java) ?: 0.0
////                            sensorData = SensorData(kelembaban, suhu, teganganAC)
//
//                            val sensorDataRequest = SensorDataRequest(suhu.toString(),
//                                kelembaban.toString(), teganganAC.toString()
//                            )
//
//                            CoroutineScope(Dispatchers.Main).launch {
//                                val sensorData = decrypt(sensorDataRequest, apiService)
//                                if (sensorData != null) {
//                                    state_suhu = sensorData.suhu
//                                    state_kelembaban = sensorData.kelembaban
//                                    state_teganganAC = sensorData.teganganAC
//                                    // Use the decrypted sensor data
//                                    Log.d("MyActivity", "Decrypted Sensor Data: $sensorData")
//                                } else {
//                                    Log.e("MyActivity", "Failed to decrypt sensor data")
//                                }
//                            }
//                        }
//                        isLoading = false
//                    }
//                    override fun onCancelled(error: DatabaseError) {
//                        // Handle possible errors
//                        isLoading = false
//                    }
//                }
//                database.addValueEventListener(dataListener)
//            }

            SuhuScreen(Modifier, this, isLoading, sensorData)
        }
    }
}

data class SensorData(
    val kelembaban: Double? = 0.0,
    val suhu: Double? = 0.0,
    val teganganAC: Double? = 0.0
)

@Composable
fun SuhuScreen(
    modifier: Modifier = Modifier,
    context: Context,
    isLoading: Boolean,
    sensorData: SensorData?
) {
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        sensorData?.let {
            Box(
                modifier = Modifier
                    .background(Color(0xFF051637))
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                        ) {
                            Text(
                                text = "${it.kelembaban}%",
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                            Text(text = "Kelembaban", color = Color.White)
                        }
                        Column(
                            modifier = Modifier
                        ) {
                            Text(
                                text = "${it.teganganAC}V",
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                            Text(text = "Tegangan AC", color = Color.White)
                        }
                        Column(
                            modifier = Modifier
                        ) {
                            Text(
                                text = "${it.suhu}C",
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 12.dp),
                            )
                            Text(text = "Suhu", color = Color.White)
                        }
                    }
                    Button(
                        modifier = Modifier
                            .width(200.dp)
                            .padding(top = 40.dp)
                            .height(40.dp),
                        onClick = {
                            AuthService().signOut(context)
                        },
                        colors = ButtonDefaults.buttonColors(Color(0xFFFF3636)),
                    ) {
                        if (isLoading) {
                            androidx.compose.material3.CircularProgressIndicator(color = Color.White)
                        } else {
                            Text(text = "Logout", color = Color.White)
                        }
                    }
                }
            }
        } ?: Text(text = "No data available")
    }
}

suspend fun decrypt(sensorDataRequest: SensorDataRequest, apiService: ApiService): SensorData? {
    return withContext(Dispatchers.IO) {
        try {
            val response = apiService.sendSensorData(sensorDataRequest)
            Log.d("SensorService", "Response: $response")
            SensorData(response.decryptedKelembaban, response.decryptedSuhu, response.decryptedTeganganAC)
        } catch (e: Exception) {
            Log.e("decrypt", "Error: ${e.message}")
            null
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    ProjektmjTheme {
    }
}