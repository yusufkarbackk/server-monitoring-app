package com.example.server_monitoring

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.server_monitoring.ui.theme.ProjektmjTheme

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