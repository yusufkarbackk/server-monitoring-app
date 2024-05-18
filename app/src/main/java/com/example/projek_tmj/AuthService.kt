package com.example.projek_tmj

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore


class AuthService {
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

    fun showToast(context: Context, message: String) {
        val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toast.show()
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
    fun login(username: String, password: String, context:Context, onLoginResult: (Boolean)-> Unit) {
        auth.signInWithEmailAndPassword("$username@tik.com", password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Log.d(TAG, "signInWithEmail:success")
                    showToast(context, "Login Success")
                    auth.currentUser
                    onLoginResult(true)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    showToast(context, "Login Failed", )

                    onLoginResult(false)
                }
            }
    }

    fun signOut(context: Context){
        auth.signOut()
        val intentAja = Intent(context, MainActivity::class.java)
        context.startActivity(intentAja)

    }
}