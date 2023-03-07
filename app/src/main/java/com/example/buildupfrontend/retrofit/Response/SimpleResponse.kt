package com.example.buildupfrontend.retrofit.Response

import com.google.gson.annotations.SerializedName

data class SimpleResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("response") val response: ResponseX,
    @SerializedName("error") val error: Any
)