package com.example.buildupfrontend.retrofit.Client

import com.example.buildupfrontend.retrofit.Request.ActivityRequest
import com.example.buildupfrontend.retrofit.Request.EditActivityImgRequest
import com.example.buildupfrontend.retrofit.Request.EditActivityRequest
import com.example.buildupfrontend.retrofit.Response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ActivityService {
    @Multipart
    @POST("/activities")
    fun post(
//        @Part categoryId: Int,
//        @Part activityName: String,
//        @Part hostName: String,
//        @Part roleName: String,
//        @Part startDate: String,
//        @Part endDate: String,
//        @Part urlName: String,
        @Part("request") request: RequestBody,
        @Part img: MultipartBody.Part
    ):Call<SimpleResponse>

    @GET("/activities/me")
    fun getAllActivities(
    ): Call<ActivityMeResponse>

    @GET("/activities/me/{categoryId}")
    fun getCategoryActivities(
        @Path ("categoryId") categoryId: Int
    ): Call<ActivityCategoryResponse>

    @GET("/activities/{activityId}")
    fun getActivityDetail(
        @Path ("activityId") activityId: Long
    ):Call<ActivityDetailResponse>

    @PUT("/activities")
    fun putEditActivity(
        @Body jsonParams: EditActivityRequest
    ):Call<SimpleResponse>

    @Multipart
    @PUT("/activities/img")
    fun putEditActivityImage(
        @Part("request") request: RequestBody,
        @Part img: MultipartBody.Part
    ): Call<SimpleResponse>

    @DELETE("/activities/{activityId}")
    fun deleteActivity(
        @Path ("activityId") activityId: Long
    ):Call<SimpleResponse>

    companion object {
        fun retrofitPost(request: RequestBody,img: MultipartBody.Part):Call<SimpleResponse>{
            return ApiClient.create(ActivityService::class.java).post(request,img)
        }
        fun retrofitActivityMe(): Call<ActivityMeResponse> {
            return ApiClient.create(ActivityService::class.java).getAllActivities()
        }
        fun retrofitCategoryActivites(categoryId: Int): Call<ActivityCategoryResponse>{
            return ApiClient.create(ActivityService::class.java).getCategoryActivities(categoryId)
        }
        fun retrofitActivityDetail(activityId: Long):Call<ActivityDetailResponse>{
            return ApiClient.create(ActivityService::class.java).getActivityDetail(activityId)
        }
        fun retrofitEditActivity(jsonParams: EditActivityRequest):Call<SimpleResponse>{
            return ApiClient.create(ActivityService::class.java).putEditActivity(jsonParams)
        }
        fun retrofitEditActivityImg(request: RequestBody,img: MultipartBody.Part):Call<SimpleResponse>{
            return ApiClient.create(ActivityService::class.java).putEditActivityImage(request,img)
        }
        fun retrofitDeleteActivity(activityId: Long):Call<SimpleResponse>{
            return ApiClient.create(ActivityService::class.java).deleteActivity(activityId)
        }
    }
}