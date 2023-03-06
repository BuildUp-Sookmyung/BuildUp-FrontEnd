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
import com.example.buildupfrontend.retrofit.SignUpProfile
import com.example.buildupfrontend.retrofit.Response.SignUpResponse
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response


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
//        nextFragment(0, FragmentSU0())
        nextFragment(3, FragmentSU3())
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
        val userInfo = UserInfoData(viewModel.userName, viewModel.userEmail,
            viewModel.checkAll, viewModel.checkService, viewModel.checkPersInfo, viewModel.checkMarketing, viewModel.checkSms, viewModel.checkEmail,
            viewModel.userID, viewModel.userPW,
            viewModel.userSchool, viewModel.userMajor, viewModel.userGrade, viewModel.userArea)
        signupUser(userInfo)
        Log.e(ContentValues.TAG, "Signup userInfo: $userInfo")
        intent.putExtra("userInfo", userInfo)
        startActivity(intent)
        finish()
    }

    private fun signupUser(userInfo: UserInfoData) {
        val api = SignUpLocalService.create()
        val profile = JSONObject()
//        profile.put("nickname", userInfo.userName)
//        profile.put("email", userInfo.userEmail)
//        profile.put("school", userInfo.userSchool)
//        profile.put("major", userInfo.userMajor)
//        profile.put("grade", userInfo.userGrade)
//        profile.put("schoolPublicYn", "N")
//        profile.put("interests", userInfo.userArea)
        profile.put("nickname", "nickname")
        profile.put("email", "marynam9912@gmail.com")
        profile.put("school", "숙명여자대학교")
        profile.put("major", "수학과")
        profile.put("grade", "4")
        profile.put("schoolPublicYn", "N")
        profile.put("interests", listOf("IT개발/데이터"))
        val suProfile = SignUpProfile("nickname", "email", "school",
        "major", "grade", "schoolPublicYn", listOf("엔지니어링/설계", "IT개발/데이터"))
        val _profile = JsonParser.parseString(profile.toString()) as JsonObject

        val map: HashMap<String, String> = HashMap()
        map["nickname"] = "nickname"
        map["email"] = "marynam9912@gmail.com"
        map["school"] = "숙명여자대학교"
        map["major"] = "수학과"
        map["grade"] = "4"
        map["schoolPublicYn"] = "N"
        map["interests"] = listOf("엔지니어링/설계", "IT개발/데이터").toString()
        val request = SignUpLocalService.body( "user ID", "user PW", _profile, "N")
        Log.i("request", request.toString())
        getRetrofitData(api, request)
    }

    private fun getRetrofitData(api: SignUpLocalService, userInfo: JsonObject) {
        api.post(userInfo)
            .enqueue(object : retrofit2.Callback<SignUpResponse?> {
                override fun onResponse(
                    call: Call<SignUpResponse?>, response: Response<SignUpResponse?>,
                ) {
                    Log.i("Response", response.toString())

                }

                override fun onFailure(p0: Call<SignUpResponse?>, p1: Throwable) {
                    TODO("Not yet implemented")
                }
            })
    }
}