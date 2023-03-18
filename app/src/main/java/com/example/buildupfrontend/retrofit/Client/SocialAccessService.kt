package com.example.buildupfrontend.retrofit.Client

import com.example.buildupfrontend.retrofit.Request.FindIDRequest
import com.example.buildupfrontend.retrofit.Request.SocialAccessRequest
import com.example.buildupfrontend.retrofit.Response.FindIDResponse
import com.example.buildupfrontend.retrofit.Response.SimpleResponse
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface SocialAccessService {

    @Headers("Content-Type:application/json")
    @POST("member/social-access")
    fun post(
        @Body userInfo: SocialAccessRequest
    ): Call<SimpleResponse>

    companion object{
        /**
         * @provider: "GOOGLE", "NAVER", "KAKAO"
         * @email: 이메일 주소
         */

        fun getRetrofit(userInfo: SocialAccessRequest): Call<SimpleResponse> {
            return ApiClient.create(SocialAccessService::class.java).post(userInfo)
        }
    }
}