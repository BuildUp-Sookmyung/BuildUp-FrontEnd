package com.example.buildupfrontend

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.buildupfrontend.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var mBinding: ActivityMainBinding? = null
    private val binding get() = mBinding!!

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userInfo: UserInfoData = intent.getSerializableExtra("userInfo") as UserInfoData
        val (userName, userEmail,
            checkAll, checkService, checkPersInfo, checkMarketing, checkSms, checkEmail,
            userID, userPW,
            userSchool, userMajor, userGrade, userArea) = userInfo

        binding.tvId.text = "ID: $userID"
        binding.tvPw.text = "PW: $userPW"
        binding.tvName.text = "Name: $userName"
        binding.tvEmail.text = "이메일: $userEmail"
    }
}