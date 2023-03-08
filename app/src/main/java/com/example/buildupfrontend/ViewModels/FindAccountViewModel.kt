package com.example.buildupfrontend.ViewModels

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class FindAccountViewModel @Inject constructor(

) : ViewModel() {
    val currentName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    @SuppressLint("StaticFieldLeak")
    lateinit var tvTop: TextView

    @SuppressLint("StaticFieldLeak")
    lateinit var tlId: TextInputLayout
    @SuppressLint("StaticFieldLeak")
    lateinit var etId: TextInputEditText
    // SU0
    lateinit var userName: String
    lateinit var userEmail: String
    // SU2
    lateinit var userID: String
    lateinit var userPW: String

    val KorEng = Pattern.compile("^[a-zA-Zㄱ-ㅎ가-힣]+$")
    val Num = Pattern.compile("^[0-9]+$")
    val Num1 = Pattern.compile("^[0-4]{1}$")
    val Num6 = Pattern.compile("^[0-9]{6}$")
    val Num11 = Pattern.compile("^[0-9]{11}$")

}