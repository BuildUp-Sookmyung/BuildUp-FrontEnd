package com.example.buildupfrontend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentPagerAdapter
import com.example.buildupfrontend.databinding.ActivitySignupBinding

//Fragment: https://korean-otter.tistory.com/entry/android-kotlin-Fragment-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0
//Progress bar: https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=kimsh2244&logNo=221069589979
class SignupActivity : AppCompatActivity() {
    private var mBinding: ActivitySignupBinding?=null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pbSignup.progress = 25 // 회원가입0 단계에서 progressbar 25%만 색칠

        setFragment()

        binding.btnBack.setOnClickListener() {
            onBackPressed()
        }

    }

    private fun setFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_signup, FragmentSU0())
            .commit()
    }
}