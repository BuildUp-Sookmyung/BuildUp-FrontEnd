package com.example.buildupfrontend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.example.buildupfrontend.databinding.ActivitySignupBinding

// Fragment: https://korean-otter.tistory.com/entry/android-kotlin-Fragment-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0
// Progress bar: https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=kimsh2244&logNo=221069589979
// Data passing between fragments: https://velog.io/@jinny_0422/Android-Fragment-Activity%EA%B0%84-%EB%8D%B0%EC%9D%B4%ED%84%B0%EC%A0%84%EB%8B%AC
class SignupActivity : AppCompatActivity() {
    private var mBinding: ActivitySignupBinding?=null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFragment(0, FragmentSU0())
//        setFragment(1, FragmentSU1())
//        setFragment(2, FragmentSU2())

        binding.btnBack.setOnClickListener() {
            onBackPressed()
        }

    }

    public fun setFragment(n:Int, fragment:Fragment) {
        if (n==0) {
            binding.pbSignup.progress = 33 // 회원가입0 단계에서 progressbar 33%만 색칠
        } else if (n==1) {
            binding.pbSignup.progress = 67 // 회원가입0 단계에서 progressbar 67%만 색칠
        } else if (n==2) {
            binding.pbSignup.progress = 100 // 회원가입0 단계에서 progressbar 67%만 색칠
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_signup, fragment)
            .commit()
    }
}