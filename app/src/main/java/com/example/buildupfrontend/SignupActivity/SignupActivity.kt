package com.example.buildupfrontend.SignupActivity

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.buildupfrontend.*
import com.example.buildupfrontend.ViewModels.SignupViewModel
import com.example.buildupfrontend.databinding.ActivitySignupBinding
import com.example.buildupfrontend.retrofit.Client.SignUpLocalService
import com.example.buildupfrontend.retrofit.Client.SignUpSocialService
import com.example.buildupfrontend.retrofit.Request.Profile
import com.example.buildupfrontend.retrofit.Request.SignUpLocalRequest
import com.example.buildupfrontend.retrofit.Request.SignUpSocialRequest
import com.example.buildupfrontend.retrofit.Response.SignUpResponse
import retrofit2.Call
import retrofit2.Response
import java.io.IOException


// Fragment: https://korean-otter.tistory.com/entry/android-kotlin-Fragment-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0
// Progress bar: https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=kimsh2244&logNo=221069589979
// Data passing between fragments: https://velog.io/@jinny_0422/Android-Fragment-Activity%EA%B0%84-%EB%8D%B0%EC%9D%B4%ED%84%B0%EC%A0%84%EB%8B%AC
open class SignupActivity : AppCompatActivity() {
    private var mBinding: ActivitySignupBinding?=null
    private val binding get() = mBinding!!
    lateinit var viewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firstView()

        binding.btnBack.setOnClickListener() {
            onBackPressed()
        }
    }

    open fun firstView() {
        viewModel = ViewModelProvider(this)[SignupViewModel::class.java]
        nextFragment(0, FragmentSU0())
//        nextFragment(3, FragmentSU3())
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
        val userInfo = UserInfoData(viewModel.provider, viewModel.accessToken, viewModel.refreshToken,
            viewModel.userName, viewModel.userEmail,
            viewModel.checkAll, viewModel.checkService, viewModel.checkPersInfo, viewModel.checkMarketing, viewModel.checkSms, viewModel.checkEmail,
            viewModel.userID, viewModel.userPW,
            viewModel.userSchool, viewModel.userMajor, viewModel.userGrade, viewModel.userArea)

        var signUpRequest: Any? = null
        if (userInfo.provider.isNullOrEmpty()) {
            signUpRequest =SignUpLocalRequest(
                username = userInfo.userID,
                password = userInfo.userPW,
                profile = Profile(
                    nickname = userInfo.userName,
                    email = userInfo.userEmail,
                    school = userInfo.userSchool,
                    major = userInfo.userMajor,
                    grade = userInfo.userGrade,
                    schoolPublicYn = "Y",
                    interests = userInfo.userArea
                ),
                emailAgreeYn = "N"
            )
            signupUser(signUpRequest, userInfo, intent)
        } else {
             signUpRequest =SignUpSocialRequest(
                 userInfo.provider,
                 profile = Profile(
                     nickname = userInfo.userName,
                     email = userInfo.userEmail,
                     school = userInfo.userSchool,
                     major = userInfo.userMajor,
                     grade = userInfo.userGrade,
                     schoolPublicYn = "Y",
                     interests = userInfo.userArea
                 ),
                 emailAgreeYn = "N"
             )
             signupUser(signUpRequest, userInfo, intent)
        }
    }

    /**
     * 일반 회원가입
     */
    private fun signupUser(signUpRequest: SignUpLocalRequest, userInfo: UserInfoData, intent: Intent) {
        SignUpLocalService.getRetrofit(signUpRequest).enqueue(object: retrofit2.Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>){
                if (response.isSuccessful) {
                    Log.e("local signup response", response.body()?.response.toString())
                    userInfo.accessToken = response.body()?.response?.accessToken
                    userInfo.refreshToken = response.body()?.response?.refreshToken
                    GlobalApplication.prefs.setString("accessToken", userInfo.accessToken!!)

                    Log.e(ContentValues.TAG, "Signup userInfo: $userInfo")
                    intent.putExtra("userInfo", userInfo)
                    startActivity(intent)
                    finish()
                }else {
                    try {
                        val body = response.body()?.response.toString()

                        Log.e(ContentValues.TAG, "body : $body")
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                Log.i("local signup failure",t.message.toString())
            }
        })
    }

    /**
     * 간편 회원가입
     */
    private fun signupUser(signUpRequest: SignUpSocialRequest, userInfo: UserInfoData, intent: Intent) {
        SignUpSocialService.getRetrofit(signUpRequest).enqueue(object: retrofit2.Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>){
                if (response.isSuccessful) {
                    Log.e("social signup response", response.body()?.response.toString())
                    userInfo.accessToken = response.body()?.response?.accessToken
                    userInfo.refreshToken = response.body()?.response?.refreshToken
                    GlobalApplication.prefs.setString("accessToken", userInfo.accessToken!!)

                    Log.e(ContentValues.TAG, "Signup userInfo: $userInfo")
                    intent.putExtra("userInfo", userInfo)
                    startActivity(intent)
                    finish()
                }else {
                    try {
                        val body = response.body()?.response.toString()

                        Log.e(ContentValues.TAG, "body : $body")
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                Log.i("social signup failure",t.message.toString())
            }
        })
    }

}