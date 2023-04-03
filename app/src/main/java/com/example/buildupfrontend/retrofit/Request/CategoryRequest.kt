package com.example.buildupfrontend.retrofit.Request

data class CategoryRequest(
    val categoryName: String,
    val iconId: Int
)

data class EditCategoryRequest(
    val id: Int,
    val categoryName: String,
    val iconId: Int
)