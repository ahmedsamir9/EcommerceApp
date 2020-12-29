package com.example.ecommerce.model

data class OrderWithProduct (val orderId:String="" ,
val productId:String="" , var quantity: Int=0 ,var productPrice:Int =0)