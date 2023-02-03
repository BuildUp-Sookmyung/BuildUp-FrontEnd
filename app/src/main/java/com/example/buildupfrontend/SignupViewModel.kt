package com.example.buildupfrontend

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.regex.Pattern
import javax.inject.Inject

// https://www.youtube.com/watch?v=wjwriHMeRZU
@HiltViewModel
class SignupViewModel @Inject constructor(

) : ViewModel() {

    // SU0
    var userName: String = ""
    var userBday: String = ""
    var userNumber: String = ""
    var userMobile: String = ""
    // SU2
    var userID: String = ""
    var userPW: String = ""

    val KorEng = Pattern.compile("^[a-zA-Zㄱ-ㅎ가-힣]+$")
    val Num = Pattern.compile("^[0-9]+$")
    val Num1 = Pattern.compile("^[0-4]{1}$")
    val Num6 = Pattern.compile("^[0-9]{6}$")
    val Num11 = Pattern.compile("^[0-9]{11}$")

}