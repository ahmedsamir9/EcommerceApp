package com.example.ecommerce.repository

import com.example.ecommerce.utils.CONSTANTS
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class MapRepo(val firestore: FirebaseFirestore) {
    suspend fun checkOutOrder(orderId:String, newOrderMap: Map<String, Any>) {
      firestore.collection(CONSTANTS.OREDER_KEY).document(orderId).set(
          newOrderMap,
          SetOptions.merge()
      ).await()
    }
}