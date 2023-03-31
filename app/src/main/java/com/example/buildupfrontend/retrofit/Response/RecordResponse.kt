package com.example.buildupfrontend.retrofit.Response

import com.google.gson.annotations.SerializedName

data class ActivityRecordResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("response") val response: ArrayList<RecordList>,
    @SerializedName("error") val error: Any
)

data class RecordList(
    @SerializedName("recordId") val recordId: Long,
    @SerializedName("title") val title: String,
    @SerializedName("date") val date: String,
)

data class RecordDetailResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("response") val response: RecordDetail,
    @SerializedName("error") val error: Any
)

data class RecordDetail(
    @SerializedName("recordId") val recordId: Long,
    @SerializedName("date") val date: String,
    @SerializedName("title") val title: String,
    @SerializedName("experience") val experience: String,
    @SerializedName("concept") val concept: String,
    @SerializedName("result") val result: String,
    @SerializedName("content") val content: String,
    @SerializedName("imgUrls") val imgUrls: ArrayList<String>,
    @SerializedName("url") val url: String,
)




