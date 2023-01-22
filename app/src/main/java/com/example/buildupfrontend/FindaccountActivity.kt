package com.example.buildupfrontend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.buildupfrontend.databinding.ActivityFindaccountBinding
import com.example.buildupfrontend.databinding.ActivitySignupBinding
import com.example.buildupfrontend.retrofit.FAAdapter
import com.google.android.material.tabs.TabLayout
//https://www.youtube.com/watch?v=ziJ6-AT3ymg
//https://snowdeer.github.io/android/2019/04/14/kotlin-viewpager-example/
class FindaccountActivity : AppCompatActivity() {
    private var mBinding: ActivityFindaccountBinding?=null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityFindaccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = FAAdapter(supportFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)

        binding.tlFindacc.setupWithViewPager(binding.vpFindacc)

        val fragmentId = FragmentId()
        val fragmentPw = FragmentPw()
        val title1 = "아이디"
        val title2 = "비밀번호"

        adapter.addFragment(fragmentId, title1)
        adapter.addFragment(fragmentPw, title2)

        binding.vpFindacc.adapter=adapter
    }
}