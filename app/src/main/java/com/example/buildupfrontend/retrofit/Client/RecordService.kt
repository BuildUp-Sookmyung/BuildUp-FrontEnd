package com.example.buildupfrontend.retrofit.Client

import com.example.buildupfrontend.retrofit.Request.RecordRequest
import com.example.buildupfrontend.retrofit.Response.ActivityRecordResponse
import com.example.buildupfrontend.retrofit.Response.RecordDetailResponse
import com.example.buildupfrontend.retrofit.Response.SimpleResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface RecordService {
    @Multipart
    @POST("/records")
    fun postRecord(
        @Part("request") request: RequestBody,
        @Part img: ArrayList<MultipartBody.Part>
    ):Call<SimpleResponse>

    @GET("/records/{recordId}")
    fun getDetailRecord(
        @Path("recordId") recordId: Long
    ):Call<RecordDetailResponse>

    @GET("/records/activities/{activityId}")
    fun getActivityRecord(
        @Path("activityId") activityId: Long
    ): Call<ActivityRecordResponse>

    @PUT("/records")
    fun editRecord(
        @Body jsonParams: RecordRequest
    ):Call<SimpleResponse>

    @Multipart
    @PUT("/records/imgs")
    fun editRecordImg(
        @Part("request") request: RequestBody,
        @Part img: ArrayList<MultipartBody.Part>
    ):Call<SimpleResponse>

    @DELETE("/records")
    fun deleteRecord(
        @Query("id") ids: ArrayList<Long>
    ):Call<SimpleResponse>

    companion object{
        fun retrofitPostRecord(request: RequestBody, img: ArrayList<MultipartBody.Part>):Call<SimpleResponse>{
            return ApiClient.create(RecordService::class.java).postRecord(request,img)
        }
        fun retrofitRecordDetail(recordId: Long):Call<RecordDetailResponse>{
            return ApiClient.create(RecordService::class.java).getDetailRecord(recordId)
        }
        fun retrofitActivityRecord(activityId: Long):Call<ActivityRecordResponse>{
            return ApiClient.create(RecordService::class.java).getActivityRecord(activityId)
        }
        fun retrofitEditRecord(jsonParams: RecordRequest):Call<SimpleResponse>{
            return ApiClient.create(RecordService::class.java).editRecord(jsonParams)
        }
        fun retrofitEditRecordImg(request: RequestBody, img: ArrayList<MultipartBody.Part>):Call<SimpleResponse>{
            return ApiClient.create(RecordService::class.java).editRecordImg(request,img)
        }
        fun retrofitDeleteRecord(id: ArrayList<Long>):Call<SimpleResponse>{
            return ApiClient.create(RecordService::class.java).deleteRecord(id)
        }
    }
}