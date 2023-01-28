package com.example.buildupfrontend

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.buildupfrontend.databinding.ActivitySignupBinding
import com.example.buildupfrontend.databinding.FragmentSu0Binding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.w3c.dom.Text
import kotlin.math.roundToInt

class FragmentSU0: Fragment() {
    lateinit var tlName: TextInputLayout
    lateinit var tlBday: TextInputLayout
    lateinit var tlNumber: TextInputLayout
    lateinit var tlMobile: TextInputLayout
    lateinit var tlVerify: TextInputLayout

    lateinit var etName: TextInputEditText
    lateinit var etBday: TextInputEditText
    lateinit var etNumber: TextInputEditText
    lateinit var etMobile: TextInputEditText
    lateinit var etVerify: TextInputEditText
    lateinit var countDownTimer: CountDownTimer
    lateinit var btnVerify: Button
    lateinit var btnOk: Button
    lateinit var tvTimer: TextView

    var validName = false
    var validBday = false
    var validNumber = false
    var validMobile = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View = inflater!!.inflate(R.layout.fragment_su0, container, false)
        tlName = view.findViewById<TextInputLayout>(R.id.tl_name)
        tlBday = view.findViewById<TextInputLayout>(R.id.tl_bday)
        tlNumber = view.findViewById<TextInputLayout>(R.id.tl_number)
        tlMobile = view.findViewById<TextInputLayout>(R.id.tl_mobile)
        tlVerify = view.findViewById<TextInputLayout>(R.id.tl_verify)

        etName = view.findViewById<TextInputEditText>(R.id.et_name)
        etBday = view.findViewById<TextInputEditText>(R.id.et_bday)
        etNumber = view.findViewById<TextInputEditText>(R.id.et_number)
        etMobile = view.findViewById<TextInputEditText>(R.id.et_mobile)
        etVerify = view.findViewById<TextInputEditText>(R.id.et_verify)

        btnVerify = view.findViewById<Button>(R.id.btn_verify)
        btnOk = view.findViewById<Button>(R.id.btn_ok)
        tvTimer = view.findViewById<TextView>(R.id.tv_timer)
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


        btnVerify.isEnabled = false // 처음에는 '인증 요청' 버튼 활성화 x
        tlVerify.visibility = View.GONE // 처음에는 '인증번호를 입력하세요'란 안 보임

        // '인증 요청' 누르기
        btnVerify.setOnClickListener() {
            start_verify()  // 입력란 나타나기, 타이머 시작, 안내 문구
        }


        tlName.editText?.onFocusChangeListener = validInput()
        tlBday.editText?.onFocusChangeListener = validInput()

        btnOk.setOnClickListener() { // 확인 버튼 누르면 다음 단계로
            signup1()
        }

        return view
    }

    private fun validInput(): View.OnFocusChangeListener {
        return View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && etName.text.toString().isNotEmpty() && etBday.text.toString().isNotEmpty()) { // 유효한 이름

            } else if (!hasFocus && txt.isEmpty()) { // 무효한 이름

            }
        }
    }

    private fun start_verify() {
        tlVerify.visibility = View.VISIBLE
        countDownTimer.start()
        tlMobile.helperText = "* 인증번호가 발송되었습니다."
    }

    private fun signup1() {
        TODO("Not yet implemented")
    }

}