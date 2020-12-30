package com.example.ecommerce.ui.profile


import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.model.User
import com.example.ecommerce.repository.ProfileRepo
import com.example.ecommerce.utils.Resources
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class ProfileViewModel @ViewModelInject constructor(private val profileRepo: ProfileRepo): ViewModel() {
    private val _user = MutableLiveData<Resources<User>>()
    val user: LiveData<Resources<User>>
        get() = _user
    fun signOut()= profileRepo.signOut()
    fun getUserData(){
        viewModelScope.launch {
            _user.postValue(Resources.loading(null))
            withContext(Dispatchers.IO){
                try {
                    val result = profileRepo.getUser()
                    for(document in result.documents) {
                        val user = document.toObject<User>()
                        _user.postValue(Resources.success(user))
                    }
                }catch (ex :Exception){
                    _user.postValue(Resources.error(ex.localizedMessage,null))
                }
            }
        }
    }
}