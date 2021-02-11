package com.example.ecommerce.ui.Login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.model.Product
import com.example.ecommerce.repository.LoginRepo
import com.example.ecommerce.utils.Resources

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginFragmentViewModel @ViewModelInject constructor(private val loginRepo: LoginRepo): ViewModel() {
    private val _message = MutableLiveData<Resources<Unit>>()
    val message: LiveData<Resources<Unit>>
        get() = _message
    fun logUser(email:String,password:String):Boolean{
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    loginRepo.logUserToFireBase(email,password)
                    _message.postValue(Resources.success(Unit))
                }
                catch (ex :Exception){
                    _message.postValue(Resources.error(ex.localizedMessage,null))
                }

            }
        }
        return loginRepo.checkLogin()
    }
    fun saveRememberOption(option:Boolean){
        loginRepo.rememberMeOption(option)
    }
    fun resetPassword(email: String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    loginRepo.forgetPassword(email)
                }
                catch (ex :Exception){
                    _message.postValue(Resources.error(ex.localizedMessage,null))
                }

            }
        }
    }
    fun checkUser(): Boolean {
        return loginRepo.checkLogin()
    }

}