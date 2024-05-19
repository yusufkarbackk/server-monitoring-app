package com.example.server_monitoring

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class UserViewModel {
    private val db = Firebase.firestore
    var auth: FirebaseAuth = Firebase.auth
    val firebaseAuth = FirebaseAuth.getInstance()
    val userStateFlow = firebaseAuth.currentUser
    fun registerUser(username: String, password: String) {
        val user = hashMapOf(
            "username" to username,
            "password" to password
        )
        db.collection("users").document(username)
            .set(user)
            .addOnSuccessListener { Log.d(TAG, "success register") }
            .addOnFailureListener { e -> Log.w(TAG, "error register", e) }
    }

    fun registerUserEmail(username: String, password: String) {
        auth.createUserWithEmailAndPassword("$username@tik.com", password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = firebaseAuth.currentUser
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                }
            }
    }
    fun login(username: String, password: String) {
        auth.signInWithEmailAndPassword("$username@tik.com", password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                }
            }
    }
}