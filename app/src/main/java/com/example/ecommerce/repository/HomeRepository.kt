package com.example.ecommerce.repository

import com.example.ecommerce.utils.CONSTANTS
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class HomeRepository(val firestore: FirebaseFirestore) {

    suspend fun getCategories()= firestore.collection(CONSTANTS.CATEGORY_KEY).get().await()
}