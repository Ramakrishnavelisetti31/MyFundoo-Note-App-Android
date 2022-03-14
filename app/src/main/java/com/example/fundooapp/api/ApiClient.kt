package com.example.fundooapp.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    private var httpLoggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
    private var okHttpClient: OkHttpClient

    init {
       val value = OkHttpClient.Builder()
        okHttpClient = value.addInterceptor(httpLoggingInterceptor).build()
    }

    private fun getRetrofit(): Retrofit {
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()
    }

    fun getService(): ApiInterface {
        return getRetrofit().create(ApiInterface::class.java)
    }

    companion object {
       private const val BASE_URL = "https://identitytoolkit.googleapis.com/v1/"
    }
 }