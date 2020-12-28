package com.example.ecommerce.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.ecommerce.model.Category
import com.example.ecommerce.model.Product
import com.example.ecommerce.repository.HomeRepository
import com.example.ecommerce.utils.Resources
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.*



class HomeViewModel @ViewModelInject constructor (private val homeRepository: HomeRepository) : ViewModel(),LifecycleObserver {
        private val _categories = MutableLiveData<Resources<List<Category>>>()
        val categories:LiveData<Resources<List<Category>>>
        get() = _categories
        private val _products = MutableLiveData<Resources<List<Product>>>()
        val products:LiveData<Resources<List<Product>>>
        get() = _products
        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun getCategories(){
            viewModelScope.launch {
                _categories.postValue(Resources.loading(null))
                withContext(Dispatchers.IO){
                    try {

                        val result = homeRepository.getCategories()
                        val categories = mutableListOf<Category>();
                        for(document in result.documents) {
                            val category = document.toObject<Category>()
                            category?.let {
                                category -> categories.add(category);
                            }
                        }
                        _categories.postValue(Resources.success(categories))
                    }
                    catch (exception : Exception){
                        _categories.postValue(Resources.error(exception.localizedMessage , null))

                    }
                }
            }
        }
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun getProducts(){
        viewModelScope.launch {
            _products.postValue(Resources.loading(null))
            withContext(Dispatchers.IO){
                try {

                    val result = homeRepository.getProducts()
                    val products = mutableListOf<Product>();
                    for(document in result.documents) {
                        val product = document.toObject<Product>()
                        product?.let {
                           products.add(it)
                        }
                    }
                    _products.postValue(Resources.success(products.shuffled()))
                }
                catch (exception : Exception){
                    _products.postValue(Resources.error(exception.localizedMessage , null))

                }
            }
        }
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun logging(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                homeRepository.login()
            }
        }

    }
}