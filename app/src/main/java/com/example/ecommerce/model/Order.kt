package com.example.ecommerce.model

data class Order (val orderId :String,
                  val  customerId:String,
                  val addressOfCustomerLongitude:Int,
                  val addressOfCustomerLatitude:Int,
                  val orderState : String
                )