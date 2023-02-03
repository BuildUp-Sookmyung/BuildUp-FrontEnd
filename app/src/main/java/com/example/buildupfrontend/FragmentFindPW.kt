package com.example.buildupfrontend

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.example.buildupfrontend.databinding.ActivityFindaccountBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

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
        tlId = mView!!.findViewById<TextInputLayout>(R.id.tl_id)
        etId = mView!!.findViewById<TextInputEditText>(R.id.et_id)

        //  '인증 요청' 누르기
        btnVerify.setOnClickListener() {
            tlId.error = null
            tlName.error = null
            tlBday.error = null
            tlNumber.error = null
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
                btnOk.isEnabled = etVerify.text.toString().isNotEmpty() && Num6.matcher(etVerify.text.toString()).find()
            }
        })
        // 확인 버튼 누르면 다음 단계로
        btnOk.setOnClickListener() {
            verifyInput(etVerify.text.toString(), verNumber)
        }
        return mView
    }

    override fun nextStep() {
        viewModel.userID = etId.text.toString()
        viewModel.userName = etName.text.toString()
        viewModel.userBday = etBday.text.toString()
        viewModel.userNumber = etNumber.text.toString()
        viewModel.userMobile = etMobile.text.toString()

        (activity as FindaccountActivity?)!!.nextFragment(FragmentFindPW2())
    }

}