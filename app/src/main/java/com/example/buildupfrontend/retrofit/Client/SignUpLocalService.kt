package com.example.buildupfrontend.retrofit.Client

import com.example.buildupfrontend.retrofit.Response.SignUpResponse
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import retrofit2.http.Headers


interface SignUpLocalService {
    @Headers("Content-Type:application/json")
    @POST("member/local")
    fun post(
        @Body userInfo: JsonObject,
    ): Call<SignUpResponse>

    companion object{
        private const val BASE_URL = "http://3.39.183.184"

        fun create(): SignUpLocalService {
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
                .create(SignUpLocalService::class.java)
        }

        /**
         * @username: 아이디
         * @password: 비밀번호
         * @profile
             * nickname: 닉네임
             * email: 이메일 인증시 입력했던 이메일 (수정 불가능)
             * school: 학교
             * major: 전공
             * grade: 학년
             * schoolPublicYn: 학교 공개여부 ("Y" 또는 "N")
             * interests: 관심분야 리스트. [ ] 안에 문자열 형태로 넣어주시면 됩니다.
         * @emailAgreeYn: 이메일 수신동의여부 ("Y" 또는 "N")
         */

        fun body(
            username: String,
            password: String,
            profile: JsonObject,
            emailAgreeYn: String
        ): JsonObject {
            val userInfo = JSONObject()
            userInfo.put("username", username)
            userInfo.put("password", password)
            userInfo.put("profile", profile)
            userInfo.put("emailAgreeYn", emailAgreeYn)

            return JsonParser.parseString(userInfo.toString()) as JsonObject
        }
    }
}