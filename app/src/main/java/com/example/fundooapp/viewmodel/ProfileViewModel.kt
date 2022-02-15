package com.example.fundooapp.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundooapp.service.AuthListener
import com.example.fundooapp.service.UserAuthService

class ProfileViewModel(val userAuthService: UserAuthService): ViewModel() {
    private val _profileStatus = MutableLiveData<AuthListener>()
    val profileStatus =  _profileStatus as LiveData<AuthListener>

    fun uploadImage(uri: Uri, context: Context) {
        userAuthService.storeImageToFireStore(uri, context)
    }

    fun getData(context: Context) {
        userAuthService.getDataFromFirestore(context) {
                _profileStatus.value = it
        }
    }
}