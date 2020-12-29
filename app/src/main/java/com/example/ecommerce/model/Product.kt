package com.example.ecommerce.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class  Product(val itemDetail :String="" ,
                   val itemId :String ="",
                   val itemImageUrl :String="" ,
                   val itemPrice:String ="",
                   val itemName :String ="",
                   var itemQuantity :String="" ,
                   val categoryId :String ="",
                   ):Parcelable
