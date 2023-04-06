package com.example.buildupfrontend.retrofit.Request

import com.google.gson.annotations.SerializedName

data class RecordRequest(
    val id: Long,
    val recordTitle: String,
    val date: String,
    val experienceName: String,
    val conceptName: String,
    val resultName: String,
    val content: String,
    val urlName: String,
)
