package com.example.ecommerce.utils

import android.content.Context
import android.view.View
import android.widget.Toast

fun View.isVisible(visible:Boolean):View{
    if (visible) this.visibility = View.VISIBLE
    else this.visibility = View.GONE
    return this
}
fun String.removeSpace():String{
    return this.replace("\\s".toRegex(), "")
}
fun Toast.showToast(ctx :Context,message:String) {
   return Toast.makeText(ctx ,message,Toast.LENGTH_LONG).show()
}