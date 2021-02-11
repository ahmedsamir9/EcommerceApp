package com.example.ecommerce.model

data class OrdersData (var orderID:String?=null,var orderState:String?=null
,var totalOfOrder:Int=0,var products: List<Product>?=null)