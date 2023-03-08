package com.example.buildupfrontend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.buildupfrontend.databinding.ActivityWelcomeBinding

import com.navercorp.nid.oauth.view.NidOAuthLoginButton.Companion.TAG
class WelcomeActivity : AppCompatActivity() {
    private var mBinding: ActivityWelcomeBinding?=null
    private val binding get() = mBinding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userInfo = intent.getSerializableExtra("userInfo") as UserInfoData
        Log.e(TAG, "Welcome Info: $userInfo")

        binding.btnStart.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("userInfo", userInfo)
            startActivity(intent)
        }
    }
}