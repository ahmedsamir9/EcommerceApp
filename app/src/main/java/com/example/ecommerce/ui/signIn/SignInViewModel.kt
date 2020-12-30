package com.example.ecommerce.ui.signIn

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.model.User
import com.example.ecommerce.repository.SignInRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignInViewModel @ViewModelInject constructor(private val signInRepo: SignInRepo) : ViewModel() {
    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message
    fun signNewUser(email:String,password:String,job:String,name:String,gender:String,address:String,birthDay:String,){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    _message.postValue("loading")
                   signInRepo.signInNewUser(email,password)
                    val user = User(name,signInRepo.firebaseAuth.currentUser!!.uid,email,birthDay,address,gender,job)
                    signInRepo.saveUser(user)
                    signInRepo.optionOnPreference()
                    _message.postValue("Done")
                }
                catch (ex :Exception){
                    _message.postValue(ex.localizedMessage)
                }

            }
        }
    }
}