package com.example.ecommerce.ui.search

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.ecommerce.model.Product
import com.example.ecommerce.repository.SearchRepo
import com.example.ecommerce.utils.Resources
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class SearchViewModel @ViewModelInject constructor(val searchRepo: SearchRepo): ViewModel() ,LifecycleObserver{
    private val _products = MutableLiveData<Resources<List<Product>>>()
    val products: LiveData<Resources<List<Product>>>
        get() = _products
   private val productList= mutableListOf<Product>()

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun getProducts(){
        viewModelScope.launch {

            withContext(Dispatchers.IO){
                try {

                    val result = searchRepo.getProducts()

                    for(document in result.documents) {
                        val product = document.toObject<Product>()
                        product?.let {
                            productList.add(product)
                        }
                    }

                }
                catch (exception : Exception){
                    _products.postValue(Resources.error(exception.localizedMessage , null))

                }
            }
        }
    }
    fun getSpecificProduct(productName:String){
        _products.postValue(Resources.loading(null))
        val productsFlitered = productList.filter {
            it.itemName.contains(productName)
        }
        _products.postValue(Resources.success(productsFlitered))
    }
}