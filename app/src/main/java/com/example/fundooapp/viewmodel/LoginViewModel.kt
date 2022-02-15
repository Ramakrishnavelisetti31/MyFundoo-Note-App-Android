package com.example.fundooapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundooapp.model.User
import com.example.fundooapp.service.AuthListener
import com.example.fundooapp.service.UserAuthService

class LoginViewModel(val userAuthService: UserAuthService): ViewModel() {
    private val _loginStatus = MutableLiveData<AuthListener>()
    val loginStatus =  _loginStatus as LiveData<AuthListener>
    private val _googleLoginStatus = MutableLiveData<AuthListener>()
    val googleLoginStatus =  _googleLoginStatus as LiveData<AuthListener>

    fun fundooLogIn(user: User) {
        userAuthService.userLogIn(user) {
            if (it.status) {
                _loginStatus.value = it
            }
        }
    }

    fun googleSignIn(idToken: String) {
        userAuthService.googleLogIn(idToken) {
            if (it.status) {
                _googleLoginStatus.value = it
            }
        }
    }
}