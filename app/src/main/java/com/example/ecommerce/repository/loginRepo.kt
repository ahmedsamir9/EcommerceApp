package com.example.ecommerce.repository

import android.content.SharedPreferences
import com.example.ecommerce.utils.CONSTANTS
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class LoginRepo(val sharedPreferences: SharedPreferences , val firebaseAuth: FirebaseAuth) {

    suspend fun logUserToFireBase (email:String, password:String) = firebaseAuth.signInWithEmailAndPassword(email, password).await()
    fun checkLogin(): Boolean {
        if (firebaseAuth.currentUser != null)return true
        return false
    }
    fun rememberMeOption(option:Boolean){
        if (option){
            sharedPreferences.edit().putString(CONSTANTS.REMEMBER_KEK_IN_PERF, CONSTANTS.REMEMBER_OPTION_REMMBERED)
                    .apply()
        }
        else{
            sharedPreferences.edit().putString(CONSTANTS.REMEMBER_KEK_IN_PERF, CONSTANTS.REMEMBER_OPTION_NOTREMMBERED)
                    .apply()
        }
    }
   fun forgetPassword(email:String)= firebaseAuth.sendPasswordResetEmail(email)
}