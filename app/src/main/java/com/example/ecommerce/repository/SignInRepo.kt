package com.example.ecommerce.repository

import android.content.SharedPreferences
import com.example.ecommerce.model.User
import com.example.ecommerce.utils.CONSTANTS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.coroutines.tasks.await

class SignInRepo (val sharedPreferences: SharedPreferences, val firebaseAuth: FirebaseAuth,val firebaseFirestore: FirebaseFirestore) {
    suspend fun saveUser(user:User)= firebaseFirestore.collection(CONSTANTS.USER_KEY).add(user).await()
    suspend fun signInNewUser(email:String,password:String)= firebaseAuth.createUserWithEmailAndPassword(email,password).await()
    fun optionOnPreference(){
        sharedPreferences.edit().putString(CONSTANTS.REMEMBER_KEK_IN_PERF, CONSTANTS.REMEMBER_OPTION_REMMBERED)
                .apply()
    }
}