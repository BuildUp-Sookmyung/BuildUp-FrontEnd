package com.example.buildupfrontend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.buildupfrontend.SignupActivity.*
import com.example.buildupfrontend.ViewModels.SignupViewModel

class LoginProfileActivity : SignupActivity() {
    lateinit var userInfoData: UserInfoData

    override fun onCreate(savedInstanceState: Bundle?) {
        userInfoData = intent.getSerializableExtra("userInfo") as UserInfoData
        super.onCreate(savedInstanceState)
        Log.i("LoginProfileActivity", userInfoData.toString())
        firstView()
    }

    override fun firstView() {
        viewModel = ViewModelProvider(this)[SignupViewModel::class.java]
        viewModel.userName = userInfoData.userName.toString()
        viewModel.userEmail = userInfoData.userEmail.toString()
        nextFragment(1, FragmentSU1())
    }
}