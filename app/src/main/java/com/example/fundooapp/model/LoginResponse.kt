package com.example.fundooapp.model

data class LoginResponse(
    var idToken: String,
    var email: String,
    var refreshToken: String,
    var expiresIn: String,
    var localId: String,
    var registered: Boolean
    )
