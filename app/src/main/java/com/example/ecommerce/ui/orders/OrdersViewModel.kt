package com.example.ecommerce.ui.orders

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.ecommerce.model.Category
import com.example.ecommerce.model.OrdersData
import com.example.ecommerce.repository.OrdersRepository
import com.example.ecommerce.utils.Resources
import kotlinx.coroutines.launch
import java.lang.Exception

class OrdersViewModel @ViewModelInject constructor(private val orderRepo:OrdersRepository): ViewModel(),LifecycleObserver{
    private val _orders=MutableLiveData<Resources<List<OrdersData>>>()
    val orders :LiveData<Resources<List<OrdersData>>>
        get() = _orders
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public fun getOrders(){
        viewModelScope.launch {
            try {
                orderRepo.getOrders()
                _orders.postValue(Resources.success(orderRepo.orderList))
            }catch (exception:Exception){
                _orders.postValue(Resources.error(exception.toString(),null))
            }
        }
    }

}