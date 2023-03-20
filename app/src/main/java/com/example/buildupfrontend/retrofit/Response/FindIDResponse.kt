package com.example.buildupfrontend.retrofit.Response

data class FindIDResponse(
    val success: Boolean,
    val response: FindIDResponseData?,
    val error: Any?
)

data class FindIDErrorResponse(
    val success: Boolean,
    val response: Any?,
    val error: FindIDError
)

data class FindIDResponseData(
    val username: String,
    val createdDate: String
)

data class FindIDError(
    val errorCode: String,
    val errorMessage: String,
    val errors: List<String>?
)