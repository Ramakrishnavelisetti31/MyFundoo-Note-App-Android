package com.example.fundooapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundooapp.model.User
import com.example.fundooapp.service.UserAuthService

class SharedViewModel(val userAuthService: UserAuthService): ViewModel() {
    private val _goToLoginPageStatus = MutableLiveData<Boolean>()
    var goToLoginPageStatus = _goToLoginPageStatus as LiveData<Boolean>

    private val _goToRegistrationPageStatus = MutableLiveData<Boolean>()
    val goToRegistrationPageStatus = _goToRegistrationPageStatus as LiveData<Boolean>

    private val _goToHomePageStatus = MutableLiveData<Boolean>()
    val goToHomePageStatus = _goToHomePageStatus as LiveData<Boolean>

    private val _userDetails = MutableLiveData<User>()
    val userDetails = _userDetails as LiveData<User>

    fun setGoToLoginPageStatus(status: Boolean) {
        _goToLoginPageStatus.value = status
    }

    fun setGoToRegistrationPageStatus(status: Boolean) {
        _goToRegistrationPageStatus.value = status
    }

    fun setGoToHomePageStatus(status: Boolean) {
        _goToHomePageStatus.value = status
    }

    fun fetchUserDetails(user: User) {
        userAuthService.getDataFromFirestore {
            _userDetails.value = it
        }
    }
}