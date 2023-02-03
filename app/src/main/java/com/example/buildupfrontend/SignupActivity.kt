package com.example.buildupfrontend

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.buildupfrontend.databinding.ActivitySignupBinding

// Fragment: https://korean-otter.tistory.com/entry/android-kotlin-Fragment-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0
// Progress bar: https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=kimsh2244&logNo=221069589979
// Data passing between fragments: https://velog.io/@jinny_0422/Android-Fragment-Activity%EA%B0%84-%EB%8D%B0%EC%9D%B4%ED%84%B0%EC%A0%84%EB%8B%AC
class SignupActivity : AppCompatActivity() {
    private var mBinding: ActivitySignupBinding?=null
    private val binding get() = mBinding!!
    private lateinit var viewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[SignupViewModel::class.java]

        nextFragment(0, FragmentSU0())
//        nextFragment(1, FragmentSU1())
//        nextFragment(2, FragmentSU2())

        binding.btnBack.setOnClickListener() {
            onBackPressed()
        }

    }

//    override fun onBackPressed(){
//        //아래와 같은 코드를 추가하도록 한다
//        //해당 엑티비티에서 띄운 프래그먼트에서 뒤로가기를 누르게 되면 프래그먼트에서 구현한 onBackPressed 함수가 실행되게 된다.
//        val fragmentList = supportFragmentManager.fragments
//        for (fragment in fragmentList) {
//            if (fragment is onBackPressedListener) {
//                (fragment as onBackPressedListener).onBackPressed()
//                return
//            }
//        }
//    }

    fun nextFragment(n:Int, fragment:Fragment) {
        if (n==0) {
            binding.tvTop.visibility = View.VISIBLE
            binding.pbSignup.progress = 33 // 회원가입0 단계에서 progressbar 33%만 색칠
        } else if (n==1) {
            binding.tvTop.visibility = View.GONE
            binding.pbSignup.progress = 67 // 회원가입0 단계에서 progressbar 67%만 색칠
        } else if (n==2) {
            binding.tvTop.visibility = View.GONE
            binding.pbSignup.progress = 100 // 회원가입0 단계에서 progressbar 67%만 색칠
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_signup, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun welcomeActivity() {
        val intent = Intent(this, WelcomeActivity::class.java)
        val userInfo = UserInfoData(viewModel.userName, viewModel.userBday, viewModel.userNumber, viewModel.userMobile, viewModel.userID, viewModel.userPW, "userEmail")
        Log.e(ContentValues.TAG, "Signup userInfo: $userInfo")
        intent.putExtra("userInfo", userInfo)
        startActivity(intent)
        finish()
    }

}