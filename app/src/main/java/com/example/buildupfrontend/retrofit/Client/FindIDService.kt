package com.example.buildupfrontend.retrofit.Client

import com.example.buildupfrontend.retrofit.Request.FindIDRequest
import com.example.buildupfrontend.retrofit.Response.FindIDResponse
import retrofit2.Call
import retrofit2.http.*

interface FindIDService {
    @Headers("Content-Type:application/json")
    @POST("member/find-id")
    fun post(
        @Body userInfo: FindIDRequest
    ): Call<FindIDResponse>

    companion object{
        fun getRetrofit(userInfo: FindIDRequest): Call<FindIDResponse> {
            return ApiClient.create(FindIDService::class.java).post(userInfo)
        }
    }
}