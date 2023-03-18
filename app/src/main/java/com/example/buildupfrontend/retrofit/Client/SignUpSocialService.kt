package com.example.buildupfrontend.retrofit.Client

import com.example.buildupfrontend.retrofit.Request.SignUpSocialRequest
import com.example.buildupfrontend.retrofit.Response.SignUpResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface SignUpSocialService {
    @Headers("Content-Type:application/json")
    @POST("member/social-profile")
    fun post(
        @Body userInfo: SignUpSocialRequest,
    ): Call<SignUpResponse>

    companion object {
        fun getRetrofit(userInfo: SignUpSocialRequest): Call<SignUpResponse> {
            return ApiClient.create(SignUpSocialService::class.java).post(userInfo)
        }
    }
}