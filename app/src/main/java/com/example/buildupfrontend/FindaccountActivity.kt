package com.example.buildupfrontend

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.buildupfrontend.databinding.ActivityFindaccountBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

//https://www.youtube.com/watch?v=ziJ6-AT3ymg
//https://snowdeer.github.io/android/2019/04/14/kotlin-viewpager-example/
class FindaccountActivity : AppCompatActivity() {
    private var mBinding: ActivityFindaccountBinding? = null
    private val binding get() = mBinding!!
    lateinit var btnId: Button
    lateinit var btnPw: Button
    lateinit var barId: View
    lateinit var barPw: View
    lateinit var flFindAccount: FrameLayout

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityFindaccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        btnId = findViewById(R.id.btn_id)
        btnPw = findViewById(R.id.btn_pw)
        barId = findViewById(R.id.bar_id)
        barPw = findViewById(R.id.bar_pw)
        flFindAccount = findViewById(R.id.fl_findAccount)
        btnId.isSelected=true

        binding.barId.setBackgroundResource(R.drawable.fill)
        nextFragment(FragmentFindID())

        // 아이디 선택
        btnId.setOnClickListener {
            fillId()
            nextFragment(FragmentFindID())
        }

        // 비밀번호 선택
        btnPw.setOnClickListener {
            fillPw()
            nextFragment(FragmentFindPW())
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

    fun nextFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_findAccount, fragment)
            .commit()
    }

    fun loginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

}
