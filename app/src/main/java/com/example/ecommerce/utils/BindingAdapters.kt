package com.example.ecommerce.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.ecommerce.R



@BindingAdapter("setImageByGlide")
fun setImageByGlide(imageView: ImageView,path:String? ){
        Glide.with(imageView.context)
            .load(path)
            .error(R.drawable.ic_box)
            .placeholder(R.drawable.ic_loader)
            .into(imageView);
}