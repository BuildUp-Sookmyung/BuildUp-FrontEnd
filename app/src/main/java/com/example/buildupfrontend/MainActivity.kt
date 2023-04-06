package com.example.buildupfrontend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
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
    private var currentFragment: Fragment? = null

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

        currentFragment = supportFragmentManager.findFragmentById(R.id.framelayout_main)

        // 다른 Activity에서 finish()로 돌아올 때
        currentFragment?.let { switchFragment(it) }

        binding.bottomnav.run {
            setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.bottomnav_home -> {
                        switchFragment(HomeFragment())
                    }
                    R.id.bottomnav_record -> {
//                        supportFragmentManager.beginTransaction()
//                            .replace(R.id.framelayout_main, RecordFragment()).commit()
                        switchFragment(RecordFragment())
                    }
                    R.id.bottomnav_search -> {
                        switchFragment(SearchFragment())
                    }
                    R.id.bottomnav_mypage -> {
                        switchFragment(MypageFragment())
                    }
                }
                true
            }
            selectedItemId = R.id.bottomnav_home
            //첫번째 탭이 선택된 상태로 있도록
        }
    }

    private fun switchFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            if (supportFragmentManager.fragments.contains(fragment)) {
                show(fragment)
            } else {
                add(R.id.framelayout_main, fragment)
            }
            currentFragment?.let {
                hide(it)
            }
            currentFragment = fragment
            commit()
        }
    }
}