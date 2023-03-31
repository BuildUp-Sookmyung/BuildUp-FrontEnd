package com.example.buildupfrontend.ViewModels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.regex.Pattern
import javax.inject.Inject

// https://www.youtube.com/watch?v=wjwriHMeRZU
@HiltViewModel
class SignupViewModel @Inject constructor(

) : ViewModel() {

    // if fragment starts after socialAccess
    var provider: String? = null
    var accessToken: String? = null
    var refreshToken: String? = null
    var createdDate: String? = null

    var validName = false
    var validEmail = false
    var validId = false
    var validPw = false

    // SU0
    var userName: String = ""
    var userEmail: String = ""
    // SU1
    var checkAll: Boolean = false
    var checkService: Boolean = false
    var checkPersInfo: Boolean = false
    var checkMarketing: Boolean = false
    var checkSms: Boolean = false
    var checkEmail: Boolean = false

    // SU2
    var userID: String = ""
    var userPW: String = ""
    // SU3
    var userSchool: String = ""
    var userMajor: String = ""
    var userGrade: String = ""
    var userArea: ArrayList<String> = arrayListOf()

}