package com.example.fundooapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundooapp.model.User
import com.example.fundooapp.service.AuthListener
import com.example.fundooapp.service.UserAuthService

class RegisterViewModel(val userAuthService: UserAuthService): ViewModel() {
    private val _registrationStatus = MutableLiveData<AuthListener>()
    val registrationStatus =  _registrationStatus as LiveData<AuthListener>

    fun fundooRegister(user: User) {
        userAuthService.registerUser(user) {
            if (it.status) {
                _registrationStatus.value = it
            }
        }
    }
}