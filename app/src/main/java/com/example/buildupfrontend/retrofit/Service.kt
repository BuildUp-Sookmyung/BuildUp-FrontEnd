package com.example.buildupfrontend.retrofit

import com.example.buildupfrontend.retrofit.ApiClient
import retrofit2.Call
import retrofit2.http.*

interface Service {
    @POST("users/signup")
    fun post(
        @Query("email") email: String,
        @Query("password") password: String,
        @Query("username") username: String
    ): Call<Response>

    companion object{
        // 다른 파일에서 통신할 때 불러올 함수명
        fun getRetrofit(email: String, password: String, username: String): Call<SignUpResponse> {
            return ApiClient.create(SignUpService::class.java).post(email, password, username)
        }
    }
}

class SignUpService {
    @POST("users/signup")
    fun post(email: String, password: String, username: String): Call<SignUpResponse> {
        TODO()
    }
}
