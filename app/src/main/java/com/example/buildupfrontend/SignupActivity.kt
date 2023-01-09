package com.example.buildupfrontend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.buildupfrontend.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    private var mBinding: ActivitySignupBinding?=null
    private val binding get() = mBinding!!


    var SignupList = arrayListOf<SignupQuestion>(
        SignupQuestion("아이디"),
        SignupQuestion("비밀번호"),
        SignupQuestion("비밀번호 확인"),
        SignupQuestion("이름"),
        SignupQuestion("닉네임"),
        SignupQuestion("이메일"),
        SignupQuestion("나이"),
        SignupQuestion("상세주소"),
        SignupQuestion("성별"),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val Adapter = SignupAdapter(this, SignupList)
        binding.liSignup.adapter = Adapter

    }
}