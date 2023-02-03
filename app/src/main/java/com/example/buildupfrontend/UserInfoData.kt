package com.example.buildupfrontend

import java.io.Serializable

data class UserInfoData(
    val userName: String?,
    val userBday:String?,
    val userNumber:String?,
    val userMobile:String?,
    val userID:String?,
    val userPW: String?,
    val userEmail:String?) : Serializable