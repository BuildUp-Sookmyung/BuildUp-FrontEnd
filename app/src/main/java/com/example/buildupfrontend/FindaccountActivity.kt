package com.example.buildupfrontend

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import android.widget.ToggleButton
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatToggleButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.example.buildupfrontend.databinding.ActivityFindaccountBinding

//https://www.youtube.com/watch?v=ziJ6-AT3ymg
//https://snowdeer.github.io/android/2019/04/14/kotlin-viewpager-example/
class FindaccountActivity : AppCompatActivity() {
    private var mBinding: ActivityFindaccountBinding? = null
    private val binding get() = mBinding!!
    lateinit var btnId: Button
    lateinit var btnPw: Button
    lateinit var barId: View
    lateinit var barPw: View

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityFindaccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        btnId = findViewById(R.id.btn_id)
        btnPw = findViewById(R.id.btn_pw)
        barId = findViewById(R.id.bar_id)
        barPw = findViewById(R.id.bar_pw)
        binding.barId.setBackgroundResource(R.drawable.fill)
        setFragment(FragmentId())

        // 아이디 선택
        btnId.setOnClickListener {
            fillId()
            setFragment(FragmentId())
        }

        // 비밀번호 선택
        btnPw.setOnClickListener {
            fillPw()
            setFragment(FragmentPw())
        }

        binding.btnBack.setOnClickListener() {
            onBackPressed()
        }

    }

    @SuppressLint("ResourceAsColor")
    private fun fillPw() {
        btnId.isSelected=false
        btnPw.isSelected=true
        barPw.setBackgroundResource(R.drawable.fill)
        barId.setBackgroundResource(R.drawable.divider)
    }

    @SuppressLint("ResourceAsColor")
    private fun fillId() {
        btnId.isSelected=true
        btnPw.isSelected=false
        barId.setBackgroundResource(R.drawable.fill)
        barPw.setBackgroundResource(R.drawable.divider)
    }

    fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_findAccount, fragment)
            .commit()
    }

}

//        val adapter = FAAdapter(supportFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
//        binding.tlFindacc.setupWithViewPager(binding.vpFindacc)
//        val fragmentId = FragmentId()
//        val fragmentPw = FragmentPw()
//        val title1 = "아이디"
//        val title2 = "비밀번호"
//        adapter.addFragment(fragmentId, title1)
//        adapter.addFragment(fragmentPw, title2)
//
//        binding.vpFindacc.adapter=adapter
//    }
//}