package com.example.ecommerce.repository

import com.example.ecommerce.utils.CONSTANTS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProfileRepo(val firestore: FirebaseFirestore ,val firebaseAuth: FirebaseAuth) {
    fun signOut()= firebaseAuth.signOut()
    suspend fun getUser()= firestore.collection(CONSTANTS.USER_KEY).whereEqualTo("id",firebaseAuth.currentUser?.uid).get().await()
}