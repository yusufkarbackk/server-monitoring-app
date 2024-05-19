package com.example.server_monitoring

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.server_monitoring.ui.theme.ProjektmjTheme
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