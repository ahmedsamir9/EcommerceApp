package com.example.ecommerce.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category constructor(val id :String="" , val name :String=""):Parcelable

