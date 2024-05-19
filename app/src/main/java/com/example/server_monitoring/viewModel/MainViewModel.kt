package com.example.server_monitoring.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainViewModel: ViewModel() {
    var isLoading = mutableStateOf(true)
    var data = mutableStateOf("")
    var errorMessage = mutableStateOf("")

    init {
        fetchData()
    }

    private fun fetchData() {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("sensorData")

        myRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                isLoading.value = false
                data.value = snapshot.value.toString()
            }
            override fun onCancelled(error: DatabaseError) {
                isLoading.value = false
                errorMessage.value = "Error: ${error.message}"
            }
        })
    }
}