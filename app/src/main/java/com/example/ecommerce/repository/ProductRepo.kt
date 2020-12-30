package com.example.ecommerce.repository

import com.example.ecommerce.model.Order
import com.example.ecommerce.model.OrderWithProduct
import com.example.ecommerce.model.Product
import com.example.ecommerce.utils.CONSTANTS
import com.example.ecommerce.utils.NetworkHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class ProductRepo(val firestore: FirebaseFirestore,val firebaseAuth: FirebaseAuth,val networkHelper: NetworkHelper) {

    suspend fun makeOrder(product: Product) {
        try {
            val userId=firebaseAuth.currentUser!!.uid
            var result =firestore.collection(CONSTANTS.OREDER_KEY).whereEqualTo("orderState",CONSTANTS.OREDER_STATE_INCOMPLETE).whereEqualTo("customerId",userId).get().await()
            if (result.isEmpty || result == null){
                firestore.collection(CONSTANTS.OREDER_KEY).add(Order(userId,0.0,0.0
                        ,CONSTANTS.OREDER_STATE_INCOMPLETE)).await()
            }
            result =firestore.collection(CONSTANTS.OREDER_KEY).whereEqualTo("orderState",CONSTANTS.OREDER_STATE_INCOMPLETE).whereEqualTo("customerId",userId).get().await()
            for(document in result.documents) {
                var res = firestore.collection(CONSTANTS.ORDER_PRODUCT_KEY).
                add(OrderWithProduct(document.id,product.itemId,product.itemQuantity.toInt(),product.itemPrice.toInt())).await()
            }

        }catch (ex:Exception){
            throw ex
        }
    }
}