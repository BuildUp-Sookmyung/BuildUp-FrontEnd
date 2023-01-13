package com.example.buildupfrontend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.buildupfrontend.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    private var mBinding: ActivitySignupBinding?=null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}