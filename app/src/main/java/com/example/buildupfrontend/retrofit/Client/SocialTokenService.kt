package com.example.buildupfrontend.retrofit.Client

import com.example.buildupfrontend.retrofit.Request.SocialTokenRequest
import com.example.buildupfrontend.retrofit.Response.TokenResponse
import retrofit2.Call
import retrofit2.http.*


interface SocialTokenService {
    @Headers("Content-Type:application/json")
    @POST("member/social-token")
    fun post(
        @Body userInfo: SocialTokenRequest
    ): Call<TokenResponse>

    companion object{
        /**
         * @provider: "GOOGLE", "NAVER", "KAKAO"
         * @email: 이메일 주소
         */
        fun getRetrofit(userInfo: SocialTokenRequest): Call<TokenResponse> {
            return ApiClient.create(SocialTokenService::class.java).post(userInfo)
        }
    }
}
