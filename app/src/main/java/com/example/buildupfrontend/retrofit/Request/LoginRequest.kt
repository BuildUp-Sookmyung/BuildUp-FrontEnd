package com.example.buildupfrontend.retrofit.Request

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val username: String,
    val password: String,
)
