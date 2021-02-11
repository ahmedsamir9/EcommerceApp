package com.example.ecommerce.repository

import com.example.ecommerce.model.Order
import com.example.ecommerce.model.OrderWithProduct
import com.example.ecommerce.model.OrdersData
import com.example.ecommerce.model.Product
import com.example.ecommerce.utils.CONSTANTS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await

class OrdersRepository(val fireStore: FirebaseFirestore, val firebaseAuth: FirebaseAuth) {
    val userId= firebaseAuth.currentUser?.uid
    val orderList = mutableListOf<OrdersData>()
    public suspend fun getOrders(){
        val result = fireStore.collection(CONSTANTS.OREDER_KEY)
            .whereNotEqualTo("orderState","incomplete")
            .whereEqualTo("customerId",userId).get().await()
        for (doc in result){
            val item = OrdersData()
            val orders = fireStore.collection(CONSTANTS.ORDER_PRODUCT_KEY)
                .whereEqualTo(CONSTANTS.ORDER_PRODUCT_Quantity,doc.id).get().await()
            item.orderID =doc.id
            var productList  = mutableListOf<Product>()
            for (order in orders){
                val orderWithProduct=order.toObject<OrderWithProduct>()
                item.orderState = "Delivered"
                item.totalOfOrder += (orderWithProduct.quantity*orderWithProduct.productPrice)
                val products = fireStore.collection(CONSTANTS.PRODUCT_KEY)
                    .whereEqualTo("itemId",orderWithProduct.productId).get().await()
                for(productItem in products){
                    val product = productItem.toObject<Product>()
                    product.itemQuantity = item.toString()
                    productList.add(product)
                }
            }
            item.products = productList
            orderList.add(item)
        }
    }
}