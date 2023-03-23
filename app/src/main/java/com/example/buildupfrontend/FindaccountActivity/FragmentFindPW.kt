package com.example.buildupfrontend.FindaccountActivity

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
import androidx.fragment.app.activityViewModels
import com.example.buildupfrontend.FragmentSharedUser
import com.example.buildupfrontend.R
import com.example.buildupfrontend.ViewModels.SignupViewModel
import com.example.buildupfrontend.retrofit.Client.EmailService
import com.example.buildupfrontend.retrofit.Client.FindPWService
import com.example.buildupfrontend.retrofit.Request.EmailRequest
import com.example.buildupfrontend.retrofit.Request.FindPWRequest
import com.example.buildupfrontend.retrofit.Response.SimpleResponse
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

@AndroidEntryPoint
class FragmentFindPW : FragmentSharedUser() {
    private val viewModel : SignupViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var timerSec = 0
        countDownTimer = object : CountDownTimer(180000, 1000) { // 총 3분, 1초마다 UI update
            override fun onTick(millisUntilFinished: Long) {
                timerSec = ((millisUntilFinished.toFloat() / 1000.0f).toInt())
                tvTimer.text = String.format("%02d:%02d".format(timerSec/60, timerSec%60)) // 2:45
            }
            override fun onFinish() {
                TODO("타이머 끝나면")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mView = inflater.inflate(R.layout.fragment_find_pw, container, false)!!
        initView(mView!!)
        // Shared Fragment에 포함 안 됨 tlId, etID 초기화

        //  '인증 요청' 누르기
        btnVerify.setOnClickListener() {
            tlId.error = null
            tlName.error = null
            btnVerify.text = "재요청"
            startVerify()  // 입력란 나타나기, 타이머 시작, 안내 문구
        }
        // 타입 검사
        checkTypes(tlId)
        // 타입과 길이까지 검사 --> 인증요청 버튼 활성화됨
        checkTypeLength(etId)
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

    override fun initView(view: View) {
        tlId = view.findViewById<TextInputLayout>(R.id.tl_id)
        tlName = view.findViewById<TextInputLayout>(R.id.tl_name)
        tlEmail = view.findViewById<TextInputLayout>(R.id.tl_email)
        tlVerify = view.findViewById<TextInputLayout>(R.id.tl_verify)

        etId = view.findViewById<TextInputEditText>(R.id.et_id)
        etName = view.findViewById<TextInputEditText>(R.id.et_name)
        etEmail = view.findViewById<TextInputEditText>(R.id.et_email)
        etVerify = view.findViewById<TextInputEditText>(R.id.et_verify)

        etId.setText(viewModel.userID)
        etName.setText(viewModel.userName)
        etEmail.setText(viewModel.userEmail)

        validName = viewModel.validName
        validEmail = viewModel.validEmail

        btnVerify = view.findViewById<Button>(R.id.btn_verify)
        btnOk = view.findViewById<Button>(R.id.btn_ok)
        tvTimer = view.findViewById<TextView>(R.id.tv_timer)

        btnVerify.isEnabled = validName && validEmail // 처음에는 '인증 요청' 버튼 활성화 x
        btnOk.isEnabled = false
        tlVerify.visibility = View.GONE // 처음에는 '인증번호를 입력하세요'란 안 보임
    }

    override fun nextStep() {
        viewModel.validName = true
        viewModel.validEmail = true
        viewModel.userID = etId.text.toString()
        viewModel.userName = etName.text.toString()
        viewModel.userEmail = etEmail.text.toString()

        (activity as FindaccountActivity?)!!.nextFragment(FragmentFindPW2())
    }

}