package com.example.ecommerce.repository

import com.example.ecommerce.model.OrderWithProduct
import com.example.ecommerce.utils.CONSTANTS
import com.example.ecommerce.utils.NetworkHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CheckoutRepo(val fireStore: FirebaseFirestore, val firebaseAuth: FirebaseAuth,val networkHelper: NetworkHelper) {
    private var userId:String = firebaseAuth.currentUser!!.uid
    suspend fun changeQuantity(orderWithProduct: OrderWithProduct,newQuantity:Int){
        val result = fireStore.collection(CONSTANTS.ORDER_PRODUCT_KEY).whereEqualTo("orderId",orderWithProduct.orderId)
                .whereEqualTo("productId",orderWithProduct.productId).get().await()
            for(document in result) {
                fireStore.collection(CONSTANTS.ORDER_PRODUCT_KEY).document(document.id).update("quantity", newQuantity).await()
            }
        }
    suspend fun deleteDocument(orderWithProduct: OrderWithProduct){
        val result = fireStore.collection(CONSTANTS.ORDER_PRODUCT_KEY).whereEqualTo("orderId",orderWithProduct.orderId)
                .whereEqualTo("productId",orderWithProduct.productId).get().await()
            for(document in result) {
                fireStore.collection(CONSTANTS.ORDER_PRODUCT_KEY).document(document.id).delete().await()
            }
    }
    suspend fun getIncompleteOrders()= fireStore.collection(CONSTANTS.OREDER_KEY).whereEqualTo("orderState",CONSTANTS.OREDER_STATE_INCOMPLETE)
            .whereEqualTo("customerId",firebaseAuth.currentUser!!.uid).get().await()
    suspend fun getOrderProduct(orderId:String)= fireStore.collection(CONSTANTS.ORDER_PRODUCT_KEY).whereEqualTo("orderId",orderId).get().await()
}
