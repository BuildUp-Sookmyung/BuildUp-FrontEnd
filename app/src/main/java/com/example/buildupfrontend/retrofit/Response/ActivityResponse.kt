package com.example.buildupfrontend.retrofit.Response

import com.google.gson.annotations.SerializedName

data class ActivityMeResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("response") val response: ArrayList<ActivityMeCheck>,
    @SerializedName("error") val error: Any
)

data class ActivityMeCheck(
    @SerializedName("activityId") val activityId: Long,
    @SerializedName("activityName") val activityName: String,
    @SerializedName("categoryName") val categoryName: String,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String,
    @SerializedName("percentage") val percentage: Int,
)

data class ActivityCategoryResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("response") val response: ArrayList<ActivityMeCheck>,
    @SerializedName("error") val error: Any
)

data class ActivityDetailResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("response") val response: ActivityDetail,
    @SerializedName("error") val error: Any
)

data class ActivityDetail(
    @SerializedName("activityId") val activityId: Long,
    @SerializedName("categoryName") val categoryName: String,
    @SerializedName("activityName") val activityName: String,
    @SerializedName("hostName") val hostName: String,
    @SerializedName("activityimg") val activityimg: String,
    @SerializedName("roleName") val roleName: String,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String,
    @SerializedName("urlName") val urlName: String,
)
