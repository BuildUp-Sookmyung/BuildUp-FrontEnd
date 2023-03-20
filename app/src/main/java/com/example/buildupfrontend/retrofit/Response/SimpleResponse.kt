package com.example.buildupfrontend.retrofit.Response

import com.google.gson.annotations.SerializedName

data class SimpleResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("response") val response: SimpleResponseData,
    @SerializedName("error") val error: SimpleResponseError?
)

data class SimpleResponseError (
    val errorCode: String,
    val errorMessage: String,
    val errors: String
)

data class SimpleResponseData(
    val message: String
)