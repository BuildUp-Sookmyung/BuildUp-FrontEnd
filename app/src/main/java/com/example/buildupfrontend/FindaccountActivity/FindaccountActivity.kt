package com.example.buildupfrontend.FindaccountActivity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.buildupfrontend.LoginActivity
import com.example.buildupfrontend.R
import com.example.buildupfrontend.databinding.ActivityFindaccountBinding
import com.example.buildupfrontend.onBackPressedListener

//https://www.youtube.com/watch?v=ziJ6-AT3ymg
//https://snowdeer.github.io/android/2019/04/14/kotlin-viewpager-example/
class FindaccountActivity : AppCompatActivity() {
    private var mBinding: ActivityFindaccountBinding? = null
    private val binding get() = mBinding!!
    lateinit var btnId: Button
    lateinit var btnPw: Button
    lateinit var barId: ImageView
    lateinit var barPw: ImageView
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

        binding.barId.setImageResource(R.drawable.fill)
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

    override fun onBackPressed(){
        //아래와 같은 코드를 추가하도록 한다
        //해당 엑티비티에서 띄운 프래그먼트에서 뒤로가기를 누르게 되면 프래그먼트에서 구현한 onBackPressed 함수가 실행되게 된다.
        val fragmentList = supportFragmentManager.fragments
        for (fragment in fragmentList) {
            if (fragment is onBackPressedListener) {
                (fragment as onBackPressedListener).onBackPressed()
                return
            }
        }
        super.onBackPressed()
    }

    @SuppressLint("ResourceAsColor")
    private fun fillPw() {
        btnId.isSelected=false
        btnPw.isSelected=true
        barPw.setImageResource(R.drawable.fill)
        barId.setImageResource(R.drawable.divider)
    }

    @SuppressLint("ResourceAsColor")
    private fun fillId() {
        btnId.isSelected=true
        btnPw.isSelected=false
        barId.setImageResource(R.drawable.fill)
        barPw.setImageResource(R.drawable.divider)
    }

    fun nextFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_findAccount, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun loginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}