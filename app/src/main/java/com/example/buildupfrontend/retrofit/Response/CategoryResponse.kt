package com.example.buildupfrontend.retrofit.Response

import com.google.gson.annotations.SerializedName

data class GetCategoryResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("response") val response: ArrayList<CategoryInfo>,
    @SerializedName("error") val error: Any
)

data class CategoryInfo(
    @SerializedName("categoryId") val categoryId: Int,
    @SerializedName("categoryName") val categoryName: String,
    @SerializedName("iconId") val iconId: Int
)