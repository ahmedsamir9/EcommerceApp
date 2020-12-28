package com.example.ecommerce.model

import android.provider.ContactsContract

data class User(val name :String,
                val id :String,
                val email: String,
                val password:String,
                val birthDay:String,
                val addressInDetails:String,
                val gender:String,
                val job:String
)