package com.example.ecommerce.ui.signIn

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.model.User
import com.example.ecommerce.repository.SignInRepo
import com.example.ecommerce.utils.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignInViewModel @ViewModelInject constructor(private val signInRepo: SignInRepo) : ViewModel() {
    private val _message = MutableLiveData<Resources<Unit>>()
    val message: LiveData<Resources<Unit>>
        get() = _message
    fun signNewUser(email:String,password:String,job:String,name:String,gender:String,address:String,birthDay:String,){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    _message.postValue(Resources.loading(null))
                   signInRepo.signInNewUser(email,password)
                    val user = User(name,signInRepo.firebaseAuth.currentUser!!.uid,email,birthDay,address,gender,job)
                    signInRepo.saveUser(user)
                    signInRepo.optionOnPreference()
                    _message.postValue(Resources.success(null))
                }
                catch (ex :Exception){
                    _message.postValue(Resources.error(ex.localizedMessage,null))
                }

            }
        }
    }
}