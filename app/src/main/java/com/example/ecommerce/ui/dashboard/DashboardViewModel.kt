package com.example.ecommerce.ui.dashboard

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.model.Order
import com.example.ecommerce.model.OrderWithProduct
import com.example.ecommerce.model.Product
import com.example.ecommerce.repository.CheckoutRepo
import com.example.ecommerce.utils.Resources
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class DashboardViewModel @ViewModelInject constructor(private val cartRepo: CheckoutRepo): ViewModel() {
    val message = "No Orders In Cart"
    private var orderId:String?= null
    private val _products = MutableLiveData<Resources<List<OrderWithProduct>>>()
    val products:LiveData<Resources<List<OrderWithProduct>>>
    get() = _products
    private val _noInterNet= MutableLiveData<String>()
    val noInterNet:LiveData<String>
        get() = _noInterNet
    suspend fun getOrders():Boolean{
            withContext(Dispatchers.IO){
                try {
                    val result= cartRepo.getIncompleteOrders()
                    if (result.isEmpty || result == null){
                        _products.postValue(Resources.error(message,null))
                    }
                    else{
                        for (doc in result.documents){
                            orderId = doc.id
                        }
                    }
                }catch (ex:Exception){
                    _products.postValue(Resources.error(message,null))
                      orderId = null
                }
            }
        orderId?.let {
            return true
        }
        return false
    }
    fun getProducts(){
        viewModelScope.launch {
            val isFoundOrder = getOrders()
            if (isFoundOrder){
                withContext(Dispatchers.IO){
                    try{
                        val result= cartRepo.getOrderProduct(orderId!!)
                        if (result.isEmpty || result == null){
                            _products.postValue(Resources.error(message,null))
                        }
                        else{
                            val productsOfOrder= mutableListOf<OrderWithProduct>()
                            for (doc in result.documents){
                                val product =doc.toObject<OrderWithProduct>()
                                productsOfOrder.add(product!!)
                            }
                            _products.postValue(Resources.success(productsOfOrder))
                        }
                    }catch (ex:Exception) {
                        _products.postValue(Resources.error(message, null))
                    }
                }
            }
        }

    }
    fun changeQuantity(newvalue: Int,item:OrderWithProduct){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    cartRepo.changeQuantity(item,newvalue)
                }
                catch (ex:Exception){
                    sentMessage("try again Latter")
                }
            }
        }
    }
    fun removeProduct(item:OrderWithProduct){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    cartRepo.deleteDocument(item)
                }
                catch (ex:Exception){
                    sentMessage("try again Latter")
                }
            }
        }
    }

    fun isConnectedToInterNet():Boolean =cartRepo.networkHelper.isNetworkConnected()
    fun sentMessage(text:String)= _noInterNet.postValue(text)
}