package com.example.buildupfrontend.retrofit.Client

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

interface FindIDService {
    @Headers("Content-Type:application/json")
    @POST("member/find-id")
    fun post(
        @Body userInfo: JsonObject
    ): Call<FindIDResponse>

    companion object{
        private const val BASE_URL = "http://3.39.183.184"

        fun create(): FindIDService {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
                        .setLevel(HttpLoggingInterceptor.Level.BODY)
                        .setLevel(HttpLoggingInterceptor.Level.HEADERS)
                )
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FindIDService::class.java)
        }

        fun body(provider: String, email: String): JsonObject {
            val jsonObject = JSONObject()
            jsonObject.put("name", provider)
            jsonObject.put("mail", email)
            return JsonParser.parseString(jsonObject.toString()) as JsonObject
        }

    }
}