package com.example.buildupfrontend.retrofit.Client

import com.example.buildupfrontend.retrofit.Request.EmailRequest
import com.example.buildupfrontend.retrofit.Request.FindIDRequest
import com.example.buildupfrontend.retrofit.Response.FindIDResponse
import com.example.buildupfrontend.retrofit.Response.SimpleResponse
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface EmailService {
    @Headers("Content-Type:application/json")
    @POST("member/email")
    fun post(
        @Body userInfo: EmailRequest
    ): Call<SimpleResponse>

    companion object{
        fun getRetrofit(userInfo: EmailRequest): Call<SimpleResponse> {
            return ApiClient.create(EmailService::class.java).post(userInfo)
        }
    }
}