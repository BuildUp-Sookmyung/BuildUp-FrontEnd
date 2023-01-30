package com.example.buildupfrontend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.buildupfrontend.databinding.ActivitySignupBinding
import com.example.buildupfrontend.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {
    private var mBinding: ActivityWelcomeBinding?=null
    private val binding get() = mBinding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStart.setOnClickListener {

        }
    }
}