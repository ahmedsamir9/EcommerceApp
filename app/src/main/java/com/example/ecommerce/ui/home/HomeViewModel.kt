package com.example.ecommerce.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.model.Category
import com.example.ecommerce.repository.HomeRepository
import com.example.ecommerce.utils.Resources
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.*
import com.example.ecommerce.model.Categories as Categories


class HomeViewModel  (private val homeRepository: HomeRepository) : ViewModel() {
        private val _categories = MutableLiveData<Resources<Categories>>()
        val categories:LiveData<Resources<Categories>>
        get() = _categories
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
}