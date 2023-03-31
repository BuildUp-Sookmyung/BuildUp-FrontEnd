package com.example.buildupfrontend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.buildupfrontend.databinding.ActivityMainBinding
import com.example.buildupfrontend.home.HomeFragment
import com.example.buildupfrontend.mypage.MypageFragment
import com.example.buildupfrontend.record.RecordFragment
import com.example.buildupfrontend.search.SearchFragment
import com.google.firebase.auth.UserInfo

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var  userInfo: UserInfoData
    private lateinit var  accessToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.getSerializableExtra("userInfo") is String) {
            accessToken = intent.getSerializableExtra("userInfo") as String
            Log.e("MainAct accessToken", accessToken)
        } else {
            userInfo = (intent.getSerializableExtra("userInfo") as UserInfoData?)!!
            Log.e("MainAct userinfo", userInfo.toString())
        }

        binding.bottomnav.run {
            setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.bottomnav_home -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.framelayout_main, HomeFragment()).commit()
                    }
                    R.id.bottomnav_record -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.framelayout_main, RecordFragment()).commit()
                    }
                    R.id.bottomnav_search -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.framelayout_main, SearchFragment()).commit()
                    }
                    R.id.bottomnav_mypage -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.framelayout_main, MypageFragment()).commit()
                    }
                }
                true
            }
            selectedItemId = R.id.bottomnav_home
            //첫번째 탭이 선택된 상태로 있도록
        }
    }
}