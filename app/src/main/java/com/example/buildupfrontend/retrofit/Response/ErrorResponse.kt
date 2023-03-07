package com.example.buildupfrontend.retrofit.Response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("errorCode") val errorCode: String,
    @SerializedName("errorMessage") val errorMessage: String,
    @SerializedName("errors") val errors: Any
)
