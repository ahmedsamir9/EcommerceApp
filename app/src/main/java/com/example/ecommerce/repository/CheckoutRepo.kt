package com.example.ecommerce.repository

import com.example.ecommerce.model.OrderWithProduct
import com.example.ecommerce.utils.CONSTANTS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class CheckoutRepo(val firestore: FirebaseFirestore,val firebaseAuth: FirebaseAuth) {
    private lateinit var userId:String
    init {
        userId =firebaseAuth.currentUser!!.uid
    }
    suspend fun changeQuantity(orderWithProduct: OrderWithProduct,newQuantity:Int){
        val result = firestore.collection(CONSTANTS.ORDER_PRODUCT_KEY).whereEqualTo(CONSTANTS.ORDER_PRODUCT_ORDER_ID,orderWithProduct.orderId)
                .whereEqualTo(CONSTANTS.ORDER_PRODUCT_PRODUCT_ID,orderWithProduct.productId).get().await()
        if(result.documents.isNotEmpty()) {
            for(document in result) {
                firestore.collection(CONSTANTS.ORDER_PRODUCT_KEY).document(document.id).update(CONSTANTS.ORDER_PRODUCT_Quantity, newQuantity).await()
            }
            }
        }
    suspend fun deleteDocument(orderWithProduct: OrderWithProduct){
        val result = firestore.collection(CONSTANTS.ORDER_PRODUCT_KEY).whereEqualTo(CONSTANTS.ORDER_PRODUCT_ORDER_ID,orderWithProduct.orderId)
                .whereEqualTo(CONSTANTS.ORDER_PRODUCT_PRODUCT_ID,orderWithProduct.productId).get().await()
        if(result.documents.isNotEmpty()) {
            for(document in result) {
                firestore.collection(CONSTANTS.ORDER_PRODUCT_KEY).document(document.id).delete().await()
            }
        }
    }
    suspend fun getUserCart(): QuerySnapshot? {
        val order = firestore.collection(CONSTANTS.OREDER_KEY).whereEqualTo("customerId",userId)
                .whereEqualTo("orderState",CONSTANTS.OREDER_STATE_INCOMPLETE)
                .get().await()
        if(order.documents.isNotEmpty()) {
            for(document in order) {
               val result= firestore.collection(CONSTANTS.ORDER_PRODUCT_KEY).whereEqualTo(CONSTANTS.ORDER_PRODUCT_ORDER_ID,document.id).get().await()
                return result
            }
        }
        return null
    }
}
