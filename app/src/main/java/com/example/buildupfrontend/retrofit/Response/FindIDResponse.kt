package com.example.buildupfrontend.retrofit.Response

import com.google.gson.annotations.SerializedName

data class FindIDResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("response") val response: ResponseFind,
    @SerializedName("error") val error: Any
)