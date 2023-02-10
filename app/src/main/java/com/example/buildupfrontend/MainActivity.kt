package com.example.buildupfrontend

import android.annotation.SuppressLint
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.buildupfrontend.databinding.ActivityMainBinding
import com.navercorp.nid.oauth.view.NidOAuthLoginButton.Companion.TAG

class MainActivity : AppCompatActivity() {
    private var mBinding: ActivityMainBinding? = null
    private val binding get() = mBinding!!

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userInfo:UserInfoData = intent.getSerializableExtra("userInfo") as UserInfoData
        val (userName, userBday, userNumber, userMobile,
            checkAll, checkService, checkPersInfo, checkMarketing, checkSms, checkEmail,
            userID, userPW,
            userEmail, userSchool, userMajor, userGrade, userArea) = userInfo

        binding.tvId.text = "ID: $userID"
        binding.tvPw.text = "PW: $userPW"
        binding.tvName.text = "Name: $userName"
        binding.tvBday.text = "주민번호: $userBday$userNumber"
        binding.tvMobile.text = "전화번호: $userMobile"
        binding.tvEmail.text = "이메일: $userEmail"
    }
}