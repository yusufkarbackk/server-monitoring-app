package com.example.projek_tmj

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.projek_tmj.ui.theme.ProjektmjTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

//class Screens : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        MyApp()
//    }
//}

enum class MonitoringScreens() {
    Login,
    Register,
    Temprature,
    Voltage,
    Humidity
}

@Composable
fun Screens() {
    val navController = rememberNavController()
    var auth: FirebaseAuth = Firebase.auth
    val firebaseAuth = FirebaseAuth.getInstance()

    val currentUser = firebaseAuth.currentUser

}

sealed class Screen(val route: String) {
    object Suhu : Screen("suhu")
    object Kelembaban : Screen("kelembaban")
    object Arus : Screen("arus")
}

//@Composable
//fun navigationGraph(startDestination: String = Screen.Suhu.route){
//    val navController = rememberNavController()
//    NavHost(navController = navController, startDestination = startDestination){
//        Composable()
//    }
//}
//
//@Composable
//fun ScreensApp() {
//    val navController = rememberNavController()
//    Scaffold(
//        bottomBar = {BottomNavBar(navController = navController)}
//    ) {
//
//    }
//}

@Composable
fun BottomNavBar(navController: NavController) {

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    ProjektmjTheme {
    }
}