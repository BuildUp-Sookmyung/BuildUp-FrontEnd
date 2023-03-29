package com.example.buildupfrontend.retrofit.Request

import com.google.gson.annotations.SerializedName

data class RecordRequest(
    @SerializedName("id") val id: Long,
    @SerializedName("recordTitle") val recordTitle: String,
    @SerializedName("date") val date: String,
    @SerializedName("experienceName") val experienceName: String,
    @SerializedName("conceptName") val conceptName: String,
    @SerializedName("resultName") val resultName: String,
    @SerializedName("content") val content: String,
    @SerializedName("urlName") val urlName: String,
)
