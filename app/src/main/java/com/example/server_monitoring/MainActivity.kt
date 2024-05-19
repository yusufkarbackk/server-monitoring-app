package com.example.server_monitoring

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var auth: FirebaseAuth = Firebase.auth

        if (auth.currentUser != null){
            val intentSuhu = Intent(this, suhu::class.java)
            startActivity(intentSuhu)
        } else{
            val intentLogin = Intent(this, LoginScreen::class.java)
            startActivity(intentLogin)
        }
    }
}


