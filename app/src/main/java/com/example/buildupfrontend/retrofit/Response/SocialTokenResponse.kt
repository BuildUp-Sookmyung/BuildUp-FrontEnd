package com.example.buildupfrontend.retrofit.Response

import com.google.gson.annotations.SerializedName

data class SocialTokenResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("response") val response: Any,
    @SerializedName("error") val error: ErrorResponse
)