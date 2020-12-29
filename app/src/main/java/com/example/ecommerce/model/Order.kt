package com.example.ecommerce.model

data class Order (
                  val  customerId:String="",
                  val addressOfCustomerLongitude:Int=0,
                  val addressOfCustomerLatitude:Int=0,
                  val orderState : String=""
                )