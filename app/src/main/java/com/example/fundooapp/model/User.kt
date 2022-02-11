package com.example.fundooapp.model

data class User(
    val fullName: String = "",
    val userId: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val imgUrl: String = ""
)

