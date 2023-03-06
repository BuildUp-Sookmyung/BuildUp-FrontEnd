package com.example.buildupfrontend

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
import com.example.buildupfrontend.retrofit.Response.SignUpResponse
import com.example.buildupfrontend.retrofit.Response.SimpleResponse
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
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


class LoginActivity : AppCompatActivity() {
    private var userID:String = "myid"
    private var userPW:String = "mypw"

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
        binding.btnLogin.isEnabled=false // 액티비티 시작 시 로그인 버튼 흐리게
        binding.cbAutologin.isChecked=true // 액티비티 시작 시 자동 로그인 디폴트 체크

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
                            }
                            else if (user != null) {
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
        //firebase auth 객체
        firebaseAuth = FirebaseAuth.getInstance()
        GoogleSignResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task)
            socialLogin("GOOGLE", userEmail)
        }

        // 아이디 입력칸 체크
        binding.etId.addTextChangedListener(object:TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.btnLogin.isEnabled = binding.etId.text.toString().isNotEmpty() and binding.etPw.text.toString().isNotEmpty()
            }
        })
        // 비밀번호 입력칸 체크
        binding.etPw.addTextChangedListener(object:TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.btnLogin.isEnabled = binding.etId.text.toString().isNotEmpty() and binding.etPw.text.toString().isNotEmpty()
            }
        })
        binding.btnLogin.setOnClickListener {
            if (validateIDPW(binding.etId.text.toString(), binding.etPw.text.toString())) {
                Thread {
                    loginToken(inputID, inputPW)
                }.start()
                nextStep(Intent(this, MainActivity::class.java))
            }
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
//        val client = OkHttpClient().newBuilder()
//            .build()
//        val mediaType: MediaType? = "application/json".toMediaTypeOrNull()
//        val body: RequestBody = "{\n    \"username\": \"kellbinnam\",\n    \"password\": \"pw1234\"\n}".toRequestBody(mediaType)
//        val request: Request = Request.Builder()
//            .url("http://3.39.183.184/member/login")
//            .addHeader("Content-Type", "application/json")
//            .method("POST", body)
//            .build()
        // 요청 전송
//        val response: Response<*> = client.newCall(request).execute()
//        //비동기 처리 (enqueue 사용)
//        client.newCall(request).enqueue(object : Callback {
//            //비동기 처리를 위해 Callback 구현
//            override fun onFailure(call: okhttp3.Call, e: IOException) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
//
//                Log.i("token response", response.toString())
//            }
//        })

        val api = LoginTokenService.create()
        val body = LoginTokenService.body(userID, userPW)
        Log.i("token body", body.toString())
        var accessToken = ""
        var refreshToken = ""

        api.post(body)
            .enqueue(object : retrofit2.Callback<SignUpResponse?> {
                override fun onResponse(
                    call: Call<SignUpResponse?>, response: Response<SignUpResponse?>,
                ) {
                    Log.i("token response", response.toString())

                    if (response.code() != 200) {
                        Log.i("token error", response.errorBody().toString())
                    } else {
                        val responseBody = response.body()!!
                        if (!responseBody.success) {
                            Log.i("response error", responseBody.error.toString())
                        } else {
                            Log.i("token", responseBody.toString())
                            accessToken = responseBody.response.accessToken
                            refreshToken = responseBody.response.refreshToken
                        }
                    }
                }

                override fun onFailure(p0: Call<SignUpResponse?>, p1: Throwable) {
                    TODO()
                }
            })
        return false
    }

    /**
     * @apiType: String - either Kakao, Naver, Google
     * @email: userEmail
     * 간편 로그인 ->
     * 1. 가입한 사용자의 경우: MainActivity 이동
     * 2. 가입 안 한 사용자의 경우: 프로필 설정(FragmentSU3 이동)
     */
    private fun socialLogin(apiType: String, email: String) {
         if (isUserSigned(apiType, email)) {
             socialToken(apiType, email)
             nextStep(Intent(this, MainActivity::class.java))
         } else {
             socialToken(apiType, email)
             setProfile(apiType)
         }
    }

    private fun socialToken(apiType: String, email: String) {
        val api = SocialTokenService.create()
        val body = SocialTokenService.body(apiType, email)
        Log.i("body", body.toString())
        var accessToken = ""
        var refreshToken = ""

        api.post(body)
            .enqueue(object : retrofit2.Callback<SignUpResponse?> {
                override fun onResponse(
                    call: Call<SignUpResponse?>, response: Response<SignUpResponse?>,
                ) {
                    Log.i("error", response.toString())

                    if (response.code() != 200) {
                        Log.i("error", response.errorBody().toString())
                    } else {
                        val responseBody = response.body()!!
                        if (!responseBody.success) {
                            Log.i("response error", responseBody.error.toString())
                        } else {
                            Log.i("token", responseBody.toString())
                            accessToken = responseBody.response.accessToken
                            refreshToken = responseBody.response.refreshToken
                        }
                    }
                }

                override fun onFailure(p0: Call<SignUpResponse?>, p1: Throwable) {
                    TODO()
                }
            })
    }

    private fun setProfile(provider: String) {
        val intent = Intent(this, LoginProfileActivity::class.java)
        intent.putExtra("provider", provider)
        nextStep(intent)
    }

    private fun isUserSigned(apiType: String, email: String): Boolean {
        val api = SocialAccessService.create()
        val body = SocialAccessService.body(apiType, email)
        var message = ""

        api.post(body)
            .enqueue(object : retrofit2.Callback<SimpleResponse?> {
                override fun onResponse(
                    call: Call<SimpleResponse?>, response: Response<SimpleResponse?>,
                ) {
                    message = if (response.code() != 200) {
                        response.errorBody().toString()
                    } else {
                        val responseBody = response.body()!!
                        if (!responseBody.success) {
                            responseBody.error.toString()
                        }
                        responseBody.response.message
                    }
                }

                override fun onFailure(p0: Call<SimpleResponse?>, error: Throwable) {
                    message = error.message.toString()
                }
            })
        Log.i("message", message)

        if ("신규" in message) {
            return false
        } else if ("이미 가입" in message) {
            return true
        }
        return false
    }

    // 아이디 유효 검사
    private fun validateIDPW(_inputID:String, _inputPW:String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return if (!checkid(_inputID)) {
            binding.tlId.error = "* 아이디를 확인해주세요."
            binding.tlPw.error = null
            return false
        } else if (checkid(_inputID) and !checkpw(_inputPW)) {
            binding.tlId.error = null
            binding.tlPw.error = "* 비밀번호를 확인해주세요."
            return false
        } else if (checkid(_inputID) and checkpw(_inputPW)) {
            binding.tlId.error = null
            binding.tlPw.error = null
            inputID = _inputID
            inputPW = _inputPW
            Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
            return true
        }
        else {
            return false
        }
    }

    private fun checkid(id: String): Boolean {
        return id==userID
    }

    private fun checkpw(pw: String): Boolean {
        return pw==userPW
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

    // onStart. 유저가 앱에 이미 구글 로그인을 했는지 확인
    public override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        // TODO: 2023-02-12 이미 로그인 되어있을시 바로 메인 액티비티로 이동
//        if (account !== null) {
//            userEmail = account?.email.toString()
//            userName = account?.givenName.toString().plus(account?.familyName.toString())
//            nextStep(firebaseAuth.currentUser)
//        }
    } //onStart End

    // onActivityResult
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)

            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("LoginActivity", "Google sign in failed", e)
            }
        }
    } // onActivityResult End

    // firebaseAuthWithGoogle
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d("LoginActivity", "firebaseAuthWithGoogle:" + acct.id!!)

        //Google SignInAccount 객체에서 ID 토큰을 가져와서 Firebase Auth로 교환하고 Firebase에 인증
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.w("LoginActivity", "firebaseAuthWithGoogle 성공", task.exception)
                    nextStep(firebaseAuth?.currentUser)
                } else {
                    Log.w("LoginActivity", "firebaseAuthWithGoogle 실패", task.exception)
                }
            }
    }// firebaseAuthWithGoogle END

    // toMainActivity
    private fun nextStep(user: FirebaseUser?) {
        val intent = Intent(this, MainActivity::class.java)
        val userInfo = UserInfoData(userName, "",
            null, null, null,null,null,null,
            inputID, inputPW,
            "", "", "", arrayListOf())
        Log.i("userInfo",userInfo.toString())
        intent.putExtra("provider", "GOOGLE")
        intent.putExtra("userInfo", userInfo)
        startActivity(intent)
        finish()
    }

    private fun nextStep(intent: Intent) {
        val userInfo = UserInfoData(userName.value(), userEmail.value(),
            null, null, null,null,null,null,
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

