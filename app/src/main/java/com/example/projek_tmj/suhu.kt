package com.example.projek_tmj

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.projek_tmj.ui.theme.ProjektmjTheme

class suhu : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjektmjTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SuhuScreen(Modifier, this)
                }
            }
        }
    }
}

@Composable
fun SuhuScreen(modifier: Modifier = Modifier, context: Context) {
    Column {
        Text(text = "Suhu")
        Button(
            modifier = Modifier
                .width(260.dp)
                .padding(top = 40.dp)
                .height(60.dp),
            onClick = {
                AuthService().signOut(context)
            },
            colors = ButtonDefaults.buttonColors(Color(0xFFFF403A)),
        ) {
            Text(text = "Logout", color = Color.White)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    ProjektmjTheme {
    }
}