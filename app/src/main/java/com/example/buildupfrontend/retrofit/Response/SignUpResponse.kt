package com.example.buildupfrontend.retrofit.Response

data class SignUpResponse(
    val success: Boolean,
    val response: SignUpToken?,
    val error: SignUpError?
)

data class SignUpToken(
    val accessToken: String,
    val refreshToken: String
)

data class SignUpError(
    val errorCode: String,
    val errorMessage: String,
    val errors: Map<String, String>?
)
