package com.example.fundooapp.model

data class User(
    var fullName: String = "",
    val userId: String = "",
    var email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    var imgUrl: String = ""
)

