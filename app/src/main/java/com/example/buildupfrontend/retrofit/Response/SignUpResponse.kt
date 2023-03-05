package com.example.buildupfrontend.retrofit.Response

import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("response") val response: ResponseToken,
    @SerializedName("error") val error: Any
)