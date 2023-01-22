package com.example.buildupfrontend.retrofit

import com.google.gson.annotations.SerializedName


data class Response(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: Int
)

data class SignUpResponse(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("username") val username: String
)


data class Request(
    val email: String,
    val password: String
)