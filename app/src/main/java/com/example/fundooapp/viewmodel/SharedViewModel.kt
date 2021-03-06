package com.example.fundooapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundooapp.service.UserAuthService

class SharedViewModel(val userAuthService: UserAuthService): ViewModel() {
    private val _goToLoginPageStatus = MutableLiveData<Boolean>()
    var goToLoginPageStatus = _goToLoginPageStatus as LiveData<Boolean>

    private val _goToRegistrationPageStatus = MutableLiveData<Boolean>()
    val goToRegistrationPageStatus = _goToRegistrationPageStatus as LiveData<Boolean>

    private val _goToHomePageStatus = MutableLiveData<Boolean>()
    val goToHomePageStatus = _goToHomePageStatus as LiveData<Boolean>

    private val _goToAddNotePage = MutableLiveData<Boolean>()
    var goToAddNotePage = _goToAddNotePage as LiveData<Boolean>

    private val _goToArchivePageStatus = MutableLiveData<Boolean>()
    val goToArchivePageStatus = _goToArchivePageStatus as LiveData<Boolean>


    fun setGoToLoginPageStatus(status: Boolean) {
        _goToLoginPageStatus.value = status
    }

    fun setGoToRegistrationPageStatus(status: Boolean) {
        _goToRegistrationPageStatus.value = status
    }

    fun setGoToHomePageStatus(status: Boolean) {
        _goToHomePageStatus.value = status
    }

    fun setGoToArchivePageStatus(status: Boolean) {
        _goToArchivePageStatus.value = status
    }

    fun setGoToAddNotePage(status: Boolean) {
        _goToAddNotePage.value = status
    }
}