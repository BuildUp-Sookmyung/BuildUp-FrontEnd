package com.example.buildupfrontend

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.buildupfrontend.FindaccountActivity.FindaccountActivity
import com.example.buildupfrontend.SignupActivity.SignupActivity
import com.example.buildupfrontend.databinding.ActivityLoginBinding
import com.example.buildupfrontend.retrofit.Client.LoginTokenService
import com.example.buildupfrontend.retrofit.Client.SocialAccessService
import com.example.buildupfrontend.retrofit.Client.SocialTokenService
import com.example.buildupfrontend.retrofit.Request.LoginRequest
import com.example.buildupfrontend.retrofit.Request.SocialAccessRequest
import com.example.buildupfrontend.retrofit.Request.SocialTokenRequest
import com.example.buildupfrontend.retrofit.Response.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import okhttp3.*
import retrofit2.Call
import retrofit2.Response
import java.io.IOException


class LoginActivity : AppCompatActivity() {
    private var userID: String = ""
    private var userPW: String = ""

    private var inputID: String = ""
    private var inputPW: String = ""

    private var mBinding: ActivityLoginBinding? = null
    private val binding get() = mBinding!!
    private var userEmail: String = ""
    private var userName: String = ""
    private var birthYear: String = ""
    private var userMobile: String = ""

    //firebase Auth
    private lateinit var firebaseAuth: FirebaseAuth

    //google client
    private lateinit var googleSignInClient: GoogleSignInClient

    //private const val TAG = "GoogleActivity"
    private val RC_SIGN_IN = 99
    private lateinit var GoogleSignResultLauncher: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnLogin.isEnabled = false // 액티비티 시작 시 로그인 버튼 흐리게
        binding.cbAutologin.isChecked = true // 액티비티 시작 시 자동 로그인 디폴트 체크

        // 1. 카카오 로그인
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
            }
        }
        binding.btnLoginKakao.setOnClickListener() {
            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        Log.e(TAG, "카카오톡으로 로그인 실패", error)

                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }
                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                    } else if (token != null) {
                        Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")

                        // 사용자 정보 요청 (추가 동의)
                        UserApiClient.instance.me { user, error ->
                            if (error != null) {
                                Log.e(TAG, "사용자 정보 요청 실패", error)
                            } else if (user != null) {
                                userName = user.kakaoAccount?.name.toString()
                                val email = user.kakaoAccount?.email.toString()
                                birthYear = user.kakaoAccount?.birthyear.toString()
                                userMobile = user.kakaoAccount?.phoneNumber.toString()
                                userEmail = email
                                socialLogin("KAKAO", userEmail)
                            }
                        }
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                socialLogin("KAKAO", userEmail)
            }
        }

        // 2. 네이버 로그인
        binding.run {
            btnLoginNaver.setOnClickListener {
                val oAuthLoginCallback = object : OAuthLoginCallback {
                    override fun onSuccess() {
                        // 네이버 로그인 API 호출 성공 시 유저 정보를 가져온다
                        NidOAuthLogin().callProfileApi(object :
                            NidProfileCallback<NidProfileResponse> {
                            override fun onSuccess(result: NidProfileResponse) {
                                userName = result.profile?.name.toString()
                                userEmail = result.profile?.email.toString()
                                birthYear = result.profile?.birthYear.toString()
                                userMobile = result.profile?.mobile.toString()

                                socialLogin("NAVER", userEmail)
                            }

                            override fun onError(errorCode: Int, message: String) {
                                //
                            }

                            override fun onFailure(httpStatus: Int, message: String) {
                                //
                            }
                        })
                    }

                    override fun onError(errorCode: Int, message: String) {
                        val naverAccessToken = NaverIdLoginSDK.getAccessToken()
                        Log.e(TAG, "naverAccessToken : $naverAccessToken")
                    }

                    override fun onFailure(httpStatus: Int, message: String) {
                        //
                    }
                }

                NaverIdLoginSDK.initialize(
                    this@LoginActivity,
                    getString(R.string.naver_client_id),
                    getString(R.string.naver_client_secret),
                    "앱 이름"
                )
                NaverIdLoginSDK.authenticate(this@LoginActivity, oAuthLoginCallback)
            }
        }

        // 3. 구글 로그인
        binding.btnLoginGoogle.setOnClickListener {
            signIn()
        }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        GoogleSignResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task)
            socialLogin("GOOGLE", userEmail)
        }

        // 아이디 입력칸 체크
        binding.etId.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.btnLogin.isEnabled =
                    binding.etId.text.toString().isNotEmpty() and binding.etPw.text.toString()
                        .isNotEmpty()
            }
        })

        // 비밀번호 입력칸 체크
        binding.etPw.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.btnLogin.isEnabled =
                    binding.etId.text.toString().isNotEmpty() and binding.etPw.text.toString()
                        .isNotEmpty()
            }
        })
        binding.btnLogin.setOnClickListener {
            loginToken(binding.etId.text.toString(), binding.etPw.text.toString())
        }
        binding.btnFindaccount.setOnClickListener {
            val intent = Intent(this, FindaccountActivity::class.java)
            startActivity(intent)
        }
        binding.btnSignup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }


    private fun loginToken(userID: String, userPW: String): Boolean {

        LoginTokenService.getRetrofit(LoginRequest(userID, userPW)).enqueue(object: retrofit2.Callback<TokenResponse> {
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>){

                if (response.isSuccessful) {
                    inputID = userID
                    inputPW = userPW
                    val accessToken = response.body()?.response?.accessToken
                    val refreshToken = response.body()?.response?.refreshToken
                    GlobalApplication.prefs.setString("accessToken", accessToken!!)
                    GlobalApplication.prefs.setString("refreshToken", refreshToken!!)

                    nextStep(null, accessToken, refreshToken, Intent(this@LoginActivity, MainActivity::class.java))

                } else {
                    try {
                        val body = Gson().fromJson(response.errorBody()!!.string(), TokenErrorResponse::class.java)
                        // "비밀번호가 틀렸습니다." or "회원을 찾을 수 없습니다."
                        if (body.error.errorCode == "CREDENTIAL_MISS_MATCH") {
                            binding.tlId.error = null
                            binding.tlPw.error = body.error.errorMessage
                        } else if (body.error.errorCode == "MEMBER_NOT_FOUND") {
                            binding.tlId.error = body.error.errorMessage
                            binding.tlPw.error = null
                        }

                        Log.e(ContentValues.TAG, "message : $body.error.errorMessage")
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                Log.i("login failure",t.message.toString())
            }
        })
        return false
    }


    private fun socialToken(apiType: String, email: String) {
        lateinit var responseBody: List<*>

        SocialTokenService.getRetrofit(SocialTokenRequest(apiType, email)).enqueue(object: retrofit2.Callback<TokenResponse> {
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>){
                responseBody = if (response.isSuccessful) {
                    listOf(true, response.body()!!.response)
                } else {
                    try {
                        val errorMessage = response.errorBody()!!.toString()
                        listOf(false, errorMessage)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        listOf(false, e.message.toString())
                    }
                }
                Log.e("socialToken", responseBody.toString())

                val accessToken = (responseBody[1] as TokenResponseData).accessToken
                val refreshToken = (responseBody[1] as TokenResponseData).refreshToken
                nextStep(apiType, accessToken, refreshToken, Intent(this@LoginActivity, MainActivity::class.java))

            }
            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                responseBody = listOf(false, t.message.toString())
            }
        })
    }

    private fun setProfile(provider: String) {
        val intent = Intent(this, LoginProfileActivity::class.java)
        nextStep(provider, null, null, intent)
    }


    /**
     * @apiType: String - either Kakao, Naver, Google
     * @email: userEmail
     * 간편 로그인 ->
     * 1. 가입한 사용자의 경우: MainActivity 이동
     * 2. 가입 안 한 사용자의 경우: 프로필 설정(FragmentSU3 이동)
     */
    private fun socialLogin(apiType: String, email: String) {
        lateinit var message: String

        SocialAccessService.getRetrofit(SocialAccessRequest(apiType, email)).enqueue(object: retrofit2.Callback<SimpleResponse> {
                override fun onResponse(
                    call: Call<SimpleResponse?>, response: Response<SimpleResponse?>,
                ) {
                    message = if (response.isSuccessful) {
                        response.body()!!.response.message
                    } else {
                        try {
                            (response.errorBody() as SimpleResponse).error?.errorMessage.toString()
                        } catch (e: IOException) {
                            e.printStackTrace().toString()
                        }
                    }
                    Log.e("socialAccess request", "$apiType $email")
                    Log.e("socialAccess response", message)

                    /**
                     * 신규 -> setProfile()
                     * 이미 가입 -> socialToken()
                     * (미구현) AUTH_PROVIDER_MISS_MATCH
                     */
                    if ("신규" in message) {
                        setProfile(apiType)

                    } else if ("가입" in message) {
                        socialToken(apiType, email)

                    }
                }

                override fun onFailure(p0: Call<SimpleResponse?>, error: Throwable) {
                    message = error.message.toString()
                    Log.e("socialAccess failure", message)
                    Toast.makeText(this@LoginActivity, "간편 로그인 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // signIn
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        GoogleSignResultLauncher.launch(signInIntent)
    }

    fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            userEmail = account?.email.toString()
            userName = account?.givenName.toString().plus(account?.familyName.toString())

        } catch (e: ApiException) {
            Log.e("Google account", "signInResult:failed Code = " + e.statusCode)
        }
    }


    private fun nextStep(provider: String?, accessToken: String?, refreshToken: String?, intent: Intent) {
        val userInfo = UserInfoData(provider, accessToken, refreshToken, userName.value(), userEmail.value(),
            null, null, null, null, null, null,
            inputID.value(), inputPW.value(),
            "", "", "", arrayListOf())

        intent.putExtra("userInfo", userInfo)
        startActivity(intent)
        finish()
    }
}

    /**
     * change any "null"s to ""
     */
    private fun String.value(): String {
        Log.i("userdata", this)
        return when {
            this.isEmpty() -> {
                ""
            }
            this=="null" -> {
                ""
            }
            else -> {
                this
            }
        }
    }

    // signIn End
//    private fun signOut() { // 로그아웃
//        // Firebase sign out
//        firebaseAuth.signOut()
//
//        // Google sign out
//        googleSignInClient.signOut().addOnCompleteListener(this) {
//            //updateUI(null)
//        }
//    }
//
//    private fun revokeAccess() { //회원탈퇴
//        // Firebase sign out
//        firebaseAuth.signOut()
//        googleSignInClient.revokeAccess().addOnCompleteListener(this) {
//
//        }
//    }

