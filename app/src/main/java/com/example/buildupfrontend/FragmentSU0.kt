package com.example.buildupfrontend

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

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

    val KorEng = Pattern.compile("^[a-zA-Zㄱ-ㅎ가-힣]+$")
    val Num = Pattern.compile("^[0-9]+$")
    val Num1 = Pattern.compile("^[0-4]{1}$")
    val Num6 = Pattern.compile("^[0-9]{6}$")
    val Num11 = Pattern.compile("^[0-9]{11}$")

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
        btnOk.isEnabled = false
        tlVerify.visibility = View.GONE // 처음에는 '인증번호를 입력하세요'란 안 보임

        var verNumber = "000000"

        // '인증 요청' 누르기
        btnVerify.setOnClickListener() {
            btnVerify.text = "재요청"
            start_verify()  // 입력란 나타나기, 타이머 시작, 안내 문구
        }

        // 타입 검사
        tlName.editText?.onFocusChangeListener = _validInput(etName, tlName, KorEng, "* 한글, 영어만 입력해주세요.")
        tlBday.editText?.onFocusChangeListener = _validInput(etBday, tlBday, Num, "* 숫자만 입력해주세요.")
        tlNumber.editText?.onFocusChangeListener = _validInput(etNumber, tlNumber, Num, "* 숫자만 입력해주세요.")
        tlMobile.editText?.onFocusChangeListener = _validInput(etMobile, tlMobile, Num, "* 올바른 핸드폰번호를 입력해주세요.")

        // 타입과 길이까지 검사
        etName.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validName = etName.text.toString().isNotEmpty() && KorEng.matcher(etName.text.toString()).find()
                btnVerify.isEnabled = validMobile && validName && validBday && validNumber
            }
        })
        etBday.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validBday = etBday.text.toString().isNotEmpty() && Num6.matcher(etBday.text.toString()).find()
                btnVerify.isEnabled = validMobile && validName && validBday && validNumber
            }
        })
        etNumber.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validNumber = etNumber.text.toString().isNotEmpty() && Num1.matcher(etNumber.text.toString()).find()
                btnVerify.isEnabled = validMobile && validName && validBday && validNumber
            }
        })
        etMobile.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validMobile = etMobile.text.toString().isNotEmpty() && Num11.matcher(etMobile.text.toString()).find()
                btnVerify.isEnabled = validMobile && validName && validBday && validNumber
            }
        })
        // --> 인증요청 버튼 활성화됨

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

        btnOk.setOnClickListener() { // 확인 버튼 누르면 다음 단계로
            verifyInput(etVerify.text.toString(), verNumber)
        }

        return view
    }

    private fun verifyInput(input:String, answer:String) {
        if (input == answer) {
            tlVerify.error = null
            signup1() // 다음 단계
        }
        else {
            tlVerify.error = "* 잘못된 인증번호입니다."
        }
    }

    private fun _validInput(et:TextInputEditText, tl:TextInputLayout, matcher:Pattern, error:String): View.OnFocusChangeListener {
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

    private fun start_verify() {
        tlVerify.visibility = View.VISIBLE
        countDownTimer.start()
        tlMobile.helperText = "* 인증번호가 발송되었습니다."
    }

    private fun signup1() {
        var fragmentSU1 = FragmentSU1()
        var bundle = Bundle()
        bundle.putStringArrayList("userInfo", arrayListOf(etName.text.toString(), etBday.text.toString(), etMobile.text.toString()))
        fragmentSU1.arguments = bundle //fragment의 arguments에 데이터를 담은 bundle을 넘겨줌

        (activity as SignupActivity?)!!.setFragment(1, fragmentSU1)
    }

}