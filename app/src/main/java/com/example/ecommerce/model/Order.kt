package com.example.ecommerce.model

data class Order (
                  val  customerId:String="",
                  val addressOfCustomerLongitude:Double=0.0,
                  val addressOfCustomerLatitude:Double=0.0,
                  val orderState : String=""
                )