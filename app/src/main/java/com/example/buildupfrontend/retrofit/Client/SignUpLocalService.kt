package com.example.buildupfrontend.retrofit.Client

import com.example.buildupfrontend.retrofit.Request.SignUpLocalRequest
import com.example.buildupfrontend.retrofit.Response.SignUpResponse
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.Headers


interface SignUpLocalService {
    @Headers("Content-Type:application/json")
    @POST("member/local")
    fun post(
        @Body userInfo: SignUpLocalRequest,
    ): Call<SignUpResponse>

    companion object {
        fun getRetrofit(userInfo: SignUpLocalRequest): Call<SignUpResponse> {
            return ApiClient.create(SignUpLocalService::class.java).post(userInfo)
        }
    }
}