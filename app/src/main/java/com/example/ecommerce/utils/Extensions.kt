package com.example.ecommerce.utils

import android.view.View

fun View.isVisible(visible:Boolean):View{
    if (visible) this.visibility = View.VISIBLE
    else this.visibility = View.GONE
    return this
}
fun String.removeSpace():String{
    return this.replace("\\s".toRegex(), "")
}