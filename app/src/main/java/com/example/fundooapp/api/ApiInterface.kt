package com.example.fundooapp.api

import com.example.fundooapp.model.LoginResponse
import com.example.fundooapp.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {
    @POST("./accounts:signInWithPassword?key=AIzaSyDsffzS_aBlUa1kdzVFCpUB_pPJWjqRiD4")
    fun login(@Body request: User): Call<LoginResponse>
}