package com.example.ecommerce.utils

import android.app.Activity
import android.app.AlertDialog
import com.example.ecommerce.R
import kotlinx.android.synthetic.main.loading.view.*

class LoaderDialog(val activity:Activity) {
    private lateinit var dialog:AlertDialog
    fun startDialog(){
       val builder:AlertDialog.Builder= AlertDialog.Builder(activity)
        val inflater =activity.layoutInflater
        builder.setCancelable(false)
        builder.setView(inflater.inflate(R.layout.loading,null))
        dialog =builder.create()
        dialog.show()
    }
    fun hideprogress(){
        dialog.dismiss()
    }
}