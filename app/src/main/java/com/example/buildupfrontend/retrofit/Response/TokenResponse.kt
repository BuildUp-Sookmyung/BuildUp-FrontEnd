package com.example.buildupfrontend.retrofit.Response

data class TokenResponse(
    val success: Boolean,
    val response: TokenResponseData?,
    val error: TokenError?
)

data class TokenErrorResponse(
    val success: Boolean,
    val response: Any?,
    val error: TokenError
)

data class TokenResponseData(
    val accessToken: String,
    val refreshToken: String
)

data class TokenError(
    val errorCode: String,
    val errorMessage: String,
    val errors: Any?
)