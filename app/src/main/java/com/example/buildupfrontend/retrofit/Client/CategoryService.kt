package com.example.buildupfrontend.retrofit.Client

import com.example.buildupfrontend.GlobalApplication
import com.example.buildupfrontend.retrofit.Request.CategoryRequest
import com.example.buildupfrontend.retrofit.Request.EditCategoryRequest
import com.example.buildupfrontend.retrofit.Response.AddCategoryResponse
import com.example.buildupfrontend.retrofit.Response.GetCategoryResponse
import retrofit2.Call
import retrofit2.http.*

interface CategoryService {

    @POST("/categories")
    fun post(
        @Body jsonParams: CategoryRequest
    ): Call<AddCategoryResponse>

    @GET("/categories")
    fun get(
    ): Call<GetCategoryResponse>

    @PUT("/categories")
    fun put(
        @Body jsonParams: EditCategoryRequest
    ):Call<AddCategoryResponse>

    @DELETE("/categories")
    fun delete(
        @Path("categoryId") categoryId: Long
    ):Call<AddCategoryResponse>

    companion object{
        fun retrofitPostCategory(jsonParams: CategoryRequest):Call<AddCategoryResponse>{
            return ApiClient.create(CategoryService::class.java).post(jsonParams)
        }

        fun retrofitGetCategory():Call<GetCategoryResponse>{
            return ApiClient.create(CategoryService::class.java).get()
        }

        fun retrofitPutCategory(jsonParams: EditCategoryRequest):Call<AddCategoryResponse>{
            return ApiClient.create(CategoryService::class.java).put(jsonParams)
        }

        fun retrofitDeleteCategory(categoryId: Long):Call<AddCategoryResponse>{
            return ApiClient.create(CategoryService::class.java).delete(categoryId)
        }
    }
}