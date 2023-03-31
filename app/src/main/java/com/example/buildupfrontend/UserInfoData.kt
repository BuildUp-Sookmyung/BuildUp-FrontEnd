package com.example.buildupfrontend

import java.io.Serializable

data class UserInfoData(
    val provider: String?,
    var accessToken: String?,
    var refreshToken: String?,
    // SU0
    val userName: String?,
    val userEmail:String?,
    // SU1
    val checkAll: Boolean?,
    val checkService: Boolean?,
    val checkPersInfo: Boolean?,
    val checkMarketing: Boolean?,
    val checkSms: Boolean?,
    val checkEmail: Boolean?,
    // SU2
    val userID:String?,
    val userPW: String?,
    // SU3
    var userSchool: String?,
    var userMajor: String?,
    var userGrade: String?,
    var userArea: ArrayList<String> = arrayListOf()

    ) : Serializable