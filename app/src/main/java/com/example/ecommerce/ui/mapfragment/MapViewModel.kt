package com.example.ecommerce.ui.mapfragment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.repository.MapRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapViewModel  @ViewModelInject constructor(private val mapRepo: MapRepo) : ViewModel() {
    fun checkOutorder(orderId :String,map:Map<String,Any>){
    viewModelScope.launch {
        try {
            withContext(Dispatchers.IO){
                mapRepo.checkOutOrder(orderId,map)
            }
        }
        catch (ex:Exception){
        }
        }
    }
}