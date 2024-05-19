package com.example.server_monitoring

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.server_monitoring.ui.theme.ProjektmjTheme
import com.example.server_monitoring.viewModel.MainViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class suhu : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                // A surface container using the 'background' color from the theme
            val mainViewModel: MainViewModel = viewModel()
            SuhuScreen(Modifier, this, mainViewModel)
        }
    }
}

data class SensorData(
    val kelembaban: Double = 0.0,
    val suhu: Double = 0.0,
    val teganganAC: Double = 0.0
)

@Composable
fun SuhuScreen(modifier: Modifier = Modifier, context: Context, viewModel: MainViewModel) {
    var sensorData by remember { mutableStateOf<SensorData?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val database = Firebase.database.getReference("sensorData")

    LaunchedEffect(Unit) {
        val dataListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val latestData = snapshot.children.last()
                    val kelembaban = latestData.child("kelembaban").getValue(Double::class.java) ?: 0.0
                    val suhu = latestData.child("suhu").getValue(Double::class.java) ?: 0.0
                    val teganganAC = latestData.child("teganganAC").getValue(Double::class.java) ?: 0.0
                    sensorData = SensorData(kelembaban, suhu, teganganAC)
                }
                isLoading = false
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors
                isLoading = false
            }
        }
        database.addValueEventListener(dataListener)
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        sensorData?.let {
            Box(modifier = Modifier.background(Color(0xFF051637)).fillMaxSize()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceEvenly) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                        ) {
                            Text(
                                text = "${it.kelembaban}",color = Color.White,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                            Text(text = "Kelembaban", color = Color.White)
                        }
                        Column(
                            modifier = Modifier
                        ) {
                            Text(
                                text = "${it.teganganAC}",color = Color.White,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                            Text(text = "Tegangan AC", color = Color.White)
                        }
                        Column(
                            modifier = Modifier
                        ) {
                            Text(text = "${it.suhu}", color = Color.White,
                                modifier = Modifier.padding(bottom = 12.dp))
                            Text(text = "Suhu:", color = Color.White)
                        }
                    }
                    Button(
                        modifier = Modifier
                            .width(260.dp)
                            .padding(top = 40.dp)
                            .height(60.dp),
                        onClick = {
                            AuthService().signOut(context)
                        },
                        colors = ButtonDefaults.buttonColors(Color(0xFFFF3636)),
                    ) {
                        if (isLoading){
                            androidx.compose.material3.CircularProgressIndicator(color = Color.White)
                        } else {
                            Text(text = "Logout", color = Color.White)
                        }
                    }
                }
            }
        } ?: Text(text = "No data available")
    }}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    ProjektmjTheme {
    }
}