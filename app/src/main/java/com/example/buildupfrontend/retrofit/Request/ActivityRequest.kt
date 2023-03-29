package com.example.buildupfrontend.retrofit.Request

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.net.URL

data class ActivityRequest(
    val categoryId: Int,
    val activityName: String,
    val hostName: String,
    val roleName: String,
    val startDate: String,
    val endDate: String,
    val urlName: String,
    val img: MultipartBody.Part
)

data class EditActivityRequest(
    val id: Long,
    val categoryId: Long,
    val activityName: String,
    val hostName: String,
    val roleName: String,
    val startDate: String,
    val endDate: String,
    val urlName: String,
)

data class EditActivityImgRequest(
    val activityId: Long,
    val img: RequestBody
)
