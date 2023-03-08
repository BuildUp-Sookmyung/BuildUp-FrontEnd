package com.example.buildupfrontend.retrofit.Client

import com.example.buildupfrontend.retrofit.Response.SignUpResponse
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
    @POST("member/login")
    fun post(
        @Body userInfo: JsonObject,
    ): Call<SignUpResponse>

    companion object{
        private const val BASE_URL = "http://3.39.183.184"

        fun create(): LoginTokenService {
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
                .create(LoginTokenService::class.java)
        }

        fun body(username: String, password: String): JsonObject {
            val jsonObject = JSONObject()
            jsonObject.put("username", username)
            jsonObject.put("password", password)
            return JsonParser.parseString(jsonObject.toString()) as JsonObject

//            val mediaType: MediaType? = "text/plain".toMediaTypeOrNull()
//            val requestBody = "{\n    \"username\": \"kellbinnam\",\n    \"password\": \"pw1234\"\n}".toRequestBody(mediaType)
//            val requestBody = jsonObject.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
//            return requestBody
        }

    }
}