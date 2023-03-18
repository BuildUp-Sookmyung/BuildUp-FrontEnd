package com.example.buildupfrontend.retrofit.Client

import com.example.buildupfrontend.retrofit.Request.LoginRequest
import com.example.buildupfrontend.retrofit.Response.TokenResponse
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface LoginTokenService {
    @Headers("Content-Type:application/json")
    @POST("/member/login")
    fun post(
        @Body userInfo: LoginRequest,
    ): Call<TokenResponse>

    companion object{
        /**
         * @username
         * @password
         */
        fun getRetrofit(userInfo: LoginRequest): Call<TokenResponse> {
            return ApiClient.create(LoginTokenService::class.java).post(userInfo)
        }

    }
}