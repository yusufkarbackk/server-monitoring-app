package com.example.projek_tmj

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.projek_tmj.ui.theme.ProjektmjTheme

@Composable
fun ProjekTmjApp() {
    val navController = rememberNavController()
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview5() {
    ProjektmjTheme {
        ProjekTmjApp()
    }
}