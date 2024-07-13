package com.example.server_monitoring

import android.content.Context
import android.content.Intent
//import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.server_monitoring.ui.theme.ProjektmjTheme

class LoginScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjektmjTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen(Modifier, this)
                }
            }
        }
    }
}

@Composable
fun LoginScreen(modifier: Modifier = Modifier, context: Context) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .background(Color(0xFF051637))
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight(0.5f)
            ) {

                Image(painter = painterResource(id = R.drawable.logo), contentDescription = null, Modifier.fillMaxSize())
            }
            Box(
                modifier = Modifier.clip(
                    shape = RoundedCornerShape(
                        topEnd = 60.dp,
                        topStart = 60.dp
                    )
                ),
            ) {
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedLabelColor = Color.Black,
                            focusedContainerColor = Color.White,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                        ),
                        value = username,
                        label = { Text(text = "Username") },
                        onValueChange = { username = it })
                    OutlinedTextField(
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedLabelColor = Color.Black,
                            focusedContainerColor = Color.White,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                        visualTransformation = PasswordVisualTransformation(),
                        value = password,
                        label = { Text(text = "Password") },
                        onValueChange = { password = it })

                    Button(
                        modifier = Modifier
                            .width(260.dp)
                            .padding(top = 40.dp)
                            .height(60.dp),
                        onClick = {
                            isLoading = true
                            AuthService().login(username, password, context) {success ->
                                isLoading = false
                                if (success){
                                    MainActivity().startAppService()
                                    val intentAja = Intent(context, suhu::class.java)
                                    context.startActivity(intentAja)
                                    Log.i("screen", "go to suhu screen from login")
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(Color(0XFF12D2FF)),
                    ) {
                        if (isLoading){
                            CircularProgressIndicator(color = Color.White)
                        } else {
                            Text(text = "Login", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

