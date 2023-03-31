package com.example.buildupfrontend

import android.content.ContentValues
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.buildupfrontend.retrofit.Client.EmailService
import com.example.buildupfrontend.retrofit.Client.LoginTokenService
import com.example.buildupfrontend.retrofit.Request.EmailRequest
import com.example.buildupfrontend.retrofit.Request.LoginRequest
import com.example.buildupfrontend.retrofit.Response.SimpleResponse
import com.example.buildupfrontend.retrofit.Response.TokenResponse
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.util.regex.Pattern


open class FragmentSharedUser(): Fragment(), onBackPressedListener, Patterns {
    var mView: View? = null

    open lateinit var tvTop: TextView
    open lateinit var tlName: TextInputLayout
    open lateinit var tlEmail: TextInputLayout
    open lateinit var tlVerify: TextInputLayout

    open lateinit var etName: TextInputEditText
    open lateinit var etEmail: TextInputEditText
    open lateinit var etVerify: TextInputEditText

    open lateinit var tlId: TextInputLayout
    open lateinit var etId: TextInputEditText

    open lateinit var countDownTimer: CountDownTimer
    open lateinit var btnVerify: Button
    open lateinit var btnOk: Button
    open lateinit var tvTimer: TextView
    open lateinit var verifyCode: String

    open var validName = false
    open var validEmail = false
    open var validId = false
    open var validPw = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var timerSec = 0
        countDownTimer = object : CountDownTimer(180000, 1000) { // 총 3분, 1초마다 UI update
            override fun onTick(millisUntilFinished: Long) {
                timerSec = ((millisUntilFinished.toFloat() / 1000.0f).toInt())
                tvTimer.text = String.format("%02d:%02d".format(timerSec/60, timerSec%60)) // 2:45
            }
            override fun onFinish() {
                verifyCode = ""
//                TODO("when time is over")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mView = inflater.inflate(R.layout.fragment_shared, container, false)!!
        initView(mView!!)
        //  '인증 요청' 누르기
        btnVerify.setOnClickListener() {
            tlName.error = null
            btnVerify.text = "재요청"
            startVerify()  // 입력란 나타나기, 타이머 시작, 안내 문구
        }
        // 타입 검사
        checkTypes()
        // 타입과 길이까지 검사 --> 인증요청 버튼 활성화됨
        checkTypeLength()
        // 인증번호 확인
        etVerify.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                btnOk.isEnabled = etVerify.text.toString().isNotEmpty()
            }
        })
        // 확인 버튼 누르면 다음 단계로
        btnOk.setOnClickListener() {
            verifyInput(etVerify.text.toString(), verifyCode)
        }
        return mView
    }

    private fun checkTypes() {
        tlName.editText?.onFocusChangeListener = validInput(etName, tlName, KorEng, "* 한글, 영어만 입력해주세요.")
        tlEmail.editText?.onFocusChangeListener = validInput(etEmail, tlEmail, typeEmail, "* 올바른 이메일을 입력해주세요.")
    }

    private fun checkTypeLength() {
        etName.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validName = etName.text.toString().isNotEmpty() && KorEng.matcher(etName.text.toString()).find()
                btnVerify.isEnabled = validEmail && validName
            }
        })
        etEmail.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validEmail = etEmail.text.toString().isNotEmpty() && typeEmail.matcher(etEmail.text.toString()).find()
                btnVerify.isEnabled = validEmail && validName
            }
        })
    }

    // 아이디까지 체크: 비밀번호 찾기의 tlId
    open fun checkTypes(tl: TextInputLayout) {
        tlName.editText?.onFocusChangeListener = validInput(etName, tlName, KorEng, "* 한글, 영어만 입력해주세요.")
        tlEmail.editText?.onFocusChangeListener = validInput(etEmail, tlEmail, typeEmail, "* 올바른 이메일을 입력해주세요.")
        tl.editText?.onFocusChangeListener = validInput(etId, tlId, typeID, "* 5~20자의 영문, 소문자, 숫자만 입력해주세요.")
    }
    open fun checkTypeLength(et: TextInputEditText) {
        etName.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validName = etName.text.toString().isNotEmpty() && KorEng.matcher(etName.text.toString()).find()
                btnVerify.isEnabled = validEmail && validName && validId
            }
        })
        etEmail.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validEmail = etEmail.text.toString().isNotEmpty() && typeEmail.matcher(etEmail.text.toString()).find()
                btnVerify.isEnabled = validEmail && validName && validId
            }
        })
        et.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validId = etId.text.toString().isNotEmpty() && typeID.matcher(etId.text.toString()).find()
                btnVerify.isEnabled = validEmail && validName && validId
            }
        })
    }

    override fun getView(): View? {
        return mView
    }

    open fun initView(view:View) {
        tvTop = view.findViewById<TextView>(R.id.tv_top)
        tlName = view.findViewById<TextInputLayout>(R.id.tl_name)
        tlEmail = view.findViewById<TextInputLayout>(R.id.tl_email)
        tlVerify = view.findViewById<TextInputLayout>(R.id.tl_verify)

        etName = view.findViewById<TextInputEditText>(R.id.et_name)
        etEmail = view.findViewById<TextInputEditText>(R.id.et_email)
        etVerify = view.findViewById<TextInputEditText>(R.id.et_verify)

        btnVerify = view.findViewById<Button>(R.id.btn_verify)
        btnOk = view.findViewById<Button>(R.id.btn_ok)
        tvTimer = view.findViewById<TextView>(R.id.tv_timer)

        btnVerify.isEnabled = false // 처음에는 '인증 요청' 버튼 활성화 x
        btnOk.isEnabled = false
        tlVerify.visibility = View.GONE // 처음에는 '인증번호를 입력하세요'란 안 보임
        tvTop.visibility = View.GONE
    }

    open fun verifyInput(input:String, answer:String) {
        if (input == answer) {
            tlVerify.error = null
            nextStep() // 다음 단계
        }
        else {
            tlVerify.error = "* 잘못된 인증번호입니다."
        }
    }

    open fun validInput(et:TextInputEditText, tl:TextInputLayout, matcher: Pattern, error:String): View.OnFocusChangeListener {
        return View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && et.text.toString() // 값이 있고 무효할 때
                    .isNotEmpty() && !matcher.matcher(et.text.toString()).find()
            ) {
                tl.error = error
            } else if (!hasFocus && et.text.toString() // 값이 있고 유효할 때
                    .isNotEmpty() && matcher.matcher(et.text.toString()).find()
            ) {
                tl.error = null
            } else if (!hasFocus && et.text.toString().isEmpty() // 값이 없을 때
            ) {
                tl.error = null
            }
        }
    }

    /*
    * 주민번호 확인할 때: 생년월일이나 뒷자리 중 하나만 틀려도 양쪽 다 빨간색 표시
     */
    open fun validNumberInput(et:TextInputEditText, tl:TextInputLayout, tl2:TextInputLayout, matcher: Pattern, error:String): View.OnFocusChangeListener {
        return View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && et.text.toString() // 값이 있고 무효할 때
                    .isNotEmpty() && !matcher.matcher(et.text.toString()).find()
            ) {
                tl.error = error
                tl2.error = " "
            } else if (!hasFocus && et.text.toString() // 값이 있고 유효할 때
                    .isNotEmpty() && matcher.matcher(et.text.toString()).find()
            ) {
                tl.error = null
                tl2.error = null
            } else if (!hasFocus && et.text.toString().isEmpty() // 값이 없을 때
            ) {
                tl.error = null
                tl2.error = null
            }
        }
    }

    override fun onBackPressed() {
        this.activity?.finish()
    }

    open fun startVerify() {
        tlVerify.visibility = View.VISIBLE
        countDownTimer.start()
        tlEmail.helperText = "* 인증번호가 발송되었습니다."
        getRetrofitData()
    }

    open fun getRetrofitData() {
        EmailService.getRetrofit(EmailRequest(etName.text.toString(), etEmail.text.toString()))
            .enqueue(object: retrofit2.Callback<SimpleResponse> {
            override fun onResponse(call: Call<SimpleResponse>, response: Response<SimpleResponse>){
                if (response.isSuccessful) {
                    if (response.code() != 200) {
                        Log.e("error", response.errorBody().toString())
                    }
                    val responseBody = response.body()!!
                    Log.e("Response", responseBody.response.toString())
                    setVerifyCode(responseBody.response.message)
                }else {
                    try {
                        val body = response.errorBody()!!.string()
                        Log.e(ContentValues.TAG, "body : $body")
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
            override fun onFailure(call: Call<SimpleResponse>, t: Throwable) {
                Log.e("email failure",t.message.toString())
            }
        })
    }

    @JvmName("setVerifyCode1")
    fun setVerifyCode(message: String) {
        verifyCode = message.split(':').last().trim()
    }

    open fun nextStep() {
    }

}