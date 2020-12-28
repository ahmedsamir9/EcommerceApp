package com.example.ecommerce.repository

import com.example.ecommerce.utils.CONSTANTS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class HomeRepository(val firestore: FirebaseFirestore,val firebaseAuth: FirebaseAuth) {
    suspend fun getCategories()= firestore.collection(CONSTANTS.CATEGORY_KEY).get().await()
    suspend fun getProducts()= firestore.collection(CONSTANTS.PRODUCT_KEY).get().await()
   suspend fun login(){
        if (firebaseAuth.currentUser == null){
            firebaseAuth.signInWithEmailAndPassword("ahmedsamir64476@gmail.com","123456789").await()
        }
    }
}