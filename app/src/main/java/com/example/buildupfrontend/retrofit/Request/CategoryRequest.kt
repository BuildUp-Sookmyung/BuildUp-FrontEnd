package com.example.buildupfrontend.retrofit.Request

data class CategoryRequest(
    val categoryName: String,
    val iconId: Long
)

data class EditCategoryRequest(
    val id: Long,
    val categoryName: String,
    val iconId: Long
)