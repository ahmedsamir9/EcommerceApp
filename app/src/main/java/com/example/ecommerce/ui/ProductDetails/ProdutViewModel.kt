package com.example.ecommerce.ui.ProductDetails

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.model.Category
import com.example.ecommerce.model.Product
import com.example.ecommerce.repository.ProductRepo
import com.example.ecommerce.utils.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.lang.Exception

class ProdutViewModel @ViewModelInject constructor(private val productRepo :ProductRepo): ViewModel() {
    private val _reqState = MutableLiveData<Resources<Unit>>()
    val reqState : LiveData<Resources<Unit>>
        get() = _reqState
    fun addProductToOrder(product: Product){
        viewModelScope.launch {
                if (productRepo.networkHelper.isNetworkConnected()){
                    withContext(Dispatchers.IO){
                        _reqState.postValue(Resources.loading(null))
                        try {
                            productRepo.makeOrder(product)
                            _reqState.postValue(Resources.success(Unit))
                        }catch (exception:Exception){
                            _reqState.postValue(Resources.error("error",null))
                        }
                    }
                }
            else
                _reqState.postValue(Resources.error("error",null))


        }
    }
}