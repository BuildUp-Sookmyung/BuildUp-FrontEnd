package com.example.buildupfrontend

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import com.example.buildupfrontend.databinding.ActivityLoginBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse

class LoginActivity : AppCompatActivity() {

    private var mBinding: ActivityLoginBinding?=null
    private val binding get() = mBinding!!
    private var email: String = ""
    private var gender: String = ""
    private var name: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.run {
            btnLoginNaver.setOnClickListener {
                val oAuthLoginCallback = object : OAuthLoginCallback {
                    override fun onSuccess() {
                        // 네이버 로그인 API 호출 성공 시 유저 정보를 가져온다
                        NidOAuthLogin().callProfileApi(object : NidProfileCallback<NidProfileResponse> {
                            override fun onSuccess(result: NidProfileResponse) {
                                name = result.profile?.name.toString()
                                email = result.profile?.email.toString()
                                gender = result.profile?.gender.toString()
                                Log.e(TAG, "네이버 로그인한 유저 정보 - 이름 : $name")
                                Log.e(TAG, "네이버 로그인한 유저 정보 - 이메일 : $email")
                                Log.e(TAG, "네이버 로그인한 유저 정보 - 성별 : $gender")
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

                NaverIdLoginSDK.initialize(this@LoginActivity, getString(R.string.naver_client_id), getString(R.string.naver_client_secret), "앱 이름")
                NaverIdLoginSDK.authenticate(this@LoginActivity, oAuthLoginCallback)
            }
        }
    }
}