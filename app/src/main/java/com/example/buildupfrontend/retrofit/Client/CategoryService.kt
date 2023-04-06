package com.example.buildupfrontend.retrofit.Client

import com.example.buildupfrontend.retrofit.Request.CategoryRequest
import com.example.buildupfrontend.retrofit.Request.EditCategoryRequest
import com.example.buildupfrontend.retrofit.Response.GetCategoryResponse
import com.example.buildupfrontend.retrofit.Response.SimpleResponse
import retrofit2.Call
import retrofit2.http.*

interface CategoryService {

    @POST("/categories")
    fun post(
        @Body jsonParams: CategoryRequest
    ): Call<SimpleResponse>

    @GET("/categories")
    fun get(
    ): Call<GetCategoryResponse>

    @PUT("/categories")
    fun put(
        @Body jsonParams: EditCategoryRequest
    ):Call<SimpleResponse>

    @DELETE("/categories/{categoryId}")
    fun delete(
        @Path("categoryId") categoryId: Long
    ):Call<SimpleResponse>

    companion object{
        fun retrofitPostCategory(jsonParams: CategoryRequest):Call<SimpleResponse>{
            return ApiClient.create(CategoryService::class.java).post(jsonParams)
        }

        fun retrofitGetCategory():Call<GetCategoryResponse>{
            return ApiClient.create(CategoryService::class.java).get()
        }

        fun retrofitPutCategory(jsonParams: EditCategoryRequest):Call<SimpleResponse>{
            return ApiClient.create(CategoryService::class.java).put(jsonParams)
        }

        fun retrofitDeleteCategory(categoryId: Long):Call<SimpleResponse>{
            return ApiClient.create(CategoryService::class.java).delete(categoryId)
        }
    }
}