package com.example.ecommerce.repository

import com.example.ecommerce.utils.CONSTANTS
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class SearchRepo(val firestore: FirebaseFirestore) {
    suspend fun getProducts()= firestore.collection(CONSTANTS.PRODUCT_KEY).get().await()

}