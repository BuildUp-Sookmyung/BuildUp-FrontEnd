package com.example.buildupfrontend.retrofit.Client

import com.example.buildupfrontend.retrofit.Request.FindPWRequest
import com.example.buildupfrontend.retrofit.Response.SimpleResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface FindPWService {
    @Headers("Content-Type:application/json")
    @POST("member/find-pw")
    fun post(
        @Body userInfo: FindPWRequest
    ): Call<SimpleResponse>

    companion object{
        fun getRetrofit(userInfo: FindPWRequest): Call<SimpleResponse> {
            return ApiClient.create(FindPWService::class.java).post(userInfo)
        }
    }
}