package com.example.ecommerce.repository

import com.example.ecommerce.utils.CONSTANTS
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CategoryRepo(private val firestore: FirebaseFirestore) {
    suspend fun getCategoriesProduct(id:String)= firestore.collection(CONSTANTS.PRODUCT_KEY).whereEqualTo("categoryId",id).get().await()
}