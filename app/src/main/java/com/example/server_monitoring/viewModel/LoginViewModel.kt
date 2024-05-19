package com.example.server_monitoring.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow

object LoginState {
    val LOGGED_IN = true
    val LOGGED_OUT = false
}

val loginStateFlow = MutableStateFlow<Boolean>(LoginState.LOGGED_OUT)

class LoginViewModel : ViewModel() {
    private val _loginState = MutableLiveData(false)
    val loginState: LiveData<Boolean> = _loginState

    init {
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.addAuthStateListener { auth -> _loginState.value = auth.currentUser != null }
    }
}