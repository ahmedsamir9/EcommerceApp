package com.example.ecommerce.ui.Category

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.ecommerce.model.Product
import com.example.ecommerce.repository.CategoryRepo
import com.example.ecommerce.utils.Resources
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class CategoryViewModel @ViewModelInject constructor(private val categoryRepo: CategoryRepo): ViewModel() {
    private val _products = MutableLiveData<Resources<List<Product>>>()
    val products: LiveData<Resources<List<Product>>>
        get() = _products
    fun getProducts(id :String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _products.postValue(Resources.loading(null))
                try {
                    val result = categoryRepo.getCategoriesProduct(id)
                    val products = mutableListOf<Product>();
                    for(document in result.documents) {
                        val product = document.toObject<Product>()
                        product?.let {
                            products.add(it)
                        }
                    }
                    _products.postValue(Resources.success(products))
                }catch (exception:Exception){
                    _products.postValue(Resources.error(exception.localizedMessage,null))
                }
            }
        }
    }
}