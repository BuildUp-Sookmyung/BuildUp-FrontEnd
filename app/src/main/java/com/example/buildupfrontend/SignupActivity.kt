package com.example.buildupfrontend

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

//        nextFragment(0, FragmentSU0())

        nextFragment(3, FragmentSU3())

        binding.btnBack.setOnClickListener() {
            onBackPressed()
        }
    }

    /**
     * 해당 엑티비티에서 띄운 프래그먼트에서 뒤로가기를 누르게 되면 프래그먼트에서 구현한 onBackPressed 함수 실행
     */
    override fun onBackPressed(){
        val fragmentList = supportFragmentManager.fragments
        for (fragment in fragmentList) {
            if (fragment is onBackPressedListener) {
                (fragment as onBackPressedListener).onBackPressed()
                return
            }
        }
        super.onBackPressed()
    }

    /**
     * @n: n번째 fragment에 대한 화면 구현
     * progress bar 색칠, 상단 멘트 유무
     */
    fun setProgressBar(n:Int) {
        binding.pbSignup.progress = 100 / 4 * (n + 1) // 회원가입0 단계에서 progressbar ( )%만 색칠
    }

    fun nextFragment(n:Int, fragment:Fragment) {
        setProgressBar(n)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_signup, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun welcomeActivity() {
        val intent = Intent(this, WelcomeActivity::class.java)
        val userInfo = UserInfoData(viewModel.userName, viewModel.userBday, viewModel.userNumber, viewModel.userMobile,
            viewModel.checkAll, viewModel.checkService, viewModel.checkPersInfo, viewModel.checkMarketing, viewModel.checkSms, viewModel.checkEmail,
            viewModel.userID, viewModel.userPW,
            viewModel.userEmail, viewModel.userSchool, viewModel.userMajor, viewModel.userGrade, viewModel.userArea)
        Log.e(ContentValues.TAG, "Signup userInfo: $userInfo")
        intent.putExtra("userInfo", userInfo)
        startActivity(intent)
        finish()
    }

}