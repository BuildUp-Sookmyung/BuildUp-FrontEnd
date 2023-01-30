package com.example.buildupfrontend

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern


class FragmentSU2: Fragment() {
    lateinit var tvTop: TextView
    lateinit var tlId: TextInputLayout
    lateinit var tlPw: TextInputLayout
    lateinit var tlPw2: TextInputLayout

    lateinit var etId: TextInputEditText
    lateinit var etPw: TextInputEditText
    lateinit var etPw2: TextInputEditText
    lateinit var btnVerify: Button
    lateinit var btnOk: Button

    var validId = false
    var validPw = false
    var matchPw = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View = inflater!!.inflate(R.layout.fragment_su2, container, false)
        val userInfo = arguments?.getStringArrayList("userInfo")
        tvTop = view.findViewById<TextView>(R.id.tv_top)
        tlId = view.findViewById<TextInputLayout>(R.id.tl_id)
        tlPw = view.findViewById<TextInputLayout>(R.id.tl_pw)
        tlPw2 = view.findViewById<TextInputLayout>(R.id.tl_pw2)
        etId = view.findViewById<TextInputEditText>(R.id.et_id)
        etPw = view.findViewById<TextInputEditText>(R.id.et_pw)
        etPw2 = view.findViewById<TextInputEditText>(R.id.et_pw2)
        btnVerify = view.findViewById<AppCompatButton>(R.id.btn_verify)
        btnOk = view.findViewById<AppCompatButton>(R.id.btn_ok)
        tvTop.text = userInfo!![0] + "님,\n사용하실 계정 정보를 입력해주세요."
        var userID = ""
        var userPW = ""

        // 초기 설정
        btnVerify.isEnabled = false
        btnOk.isEnabled = false

        // 아이디 길이만 검사 -> 중복 확인 버튼 활성화
        etId.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                btnVerify.isEnabled = etId.text.toString().length > 5
            }
        })

        // '중복 확인' -> 아이디 타입 검사 후 중복 검사
        btnVerify.setOnClickListener {
            var checkId = etId.text.toString()
            if (!isRegularID(checkId)) {
                tlId.error = "* 5~20자의 영문, 소문자, 숫자만 사용해주세요."
            } else if (!usableID(checkId)) {
                tlId.error = "* 이미 사용중인 아이디입니다."
            } else {
                tlId.error = null
                tlId.helperText = "* 사용 가능한 아이디입니다."
            }
        }

        // 비밀번호 타입 검사
        tlPw.editText?.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            val checkPw = etPw.text.toString()
            if (!hasFocus && etPw.text.toString() // 값이 있고 무효할 때
                    .isNotEmpty() && !isRegularPW(checkPw)
            ) {
                tlPw.error = "* 6~16자의 영문 대소문자, 숫자, 특수문자 중 2개 이상 사용해주세요."
            } else if (!hasFocus && etPw.text.toString() // 값이 있고 유효할 때
                    .isNotEmpty() && isRegularPW(checkPw)
            ) {
                tlPw.error = null
            } else if (!hasFocus && etPw.text.toString().isEmpty() // 값이 없을 때
            ) {
                tlPw.error = null
            }
        }

        // 비밀번호, 비밀번호 확인 입력 길이 같으면 '가입완료' 활성화
        etPw2.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val checkPw = etPw.text.toString()
                val checkPw2 = etPw2.text.toString()
                btnOk.isEnabled = checkPw.length == checkPw2.length
            }
        })

        // '가입완료' 눌렀을 때 비밀번호 일치 여부 확인
        btnOk.setOnClickListener {
            val checkPw = etPw.text.toString()
            val checkPw2 = etPw2.text.toString()
            if (checkPw != checkPw2) {
                tlPw2.error = "* 비밀번호가 일치하지 않습니다."
            } else {
                userID = etId.text.toString()
                userPW = checkPw
                welcomePage()
                TODO("ID, PW, 사용자 정보 넘기기")
            }
        }
        return view
    }

    private fun welcomePage() {
        val intent =Intent(this.context, WelcomeActivity::class.java)
        startActivity(intent)
    }

    private fun usableID(checkId: String): Boolean {
        return true
        TODO("check if ID exists")
    }

    private fun isRegularID(id: String): Boolean {
        val pwPattern = "^[a-z0-9]{5,20}+$"

        return (Pattern.matches(pwPattern, id))
    }

    private fun isRegularPW(password: String): Boolean {
        val pwPattern1 = "^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#\$%^&*])(?=.*[0-9!@#\$%^&*]).{6,16}\$" // 영문, 숫자, 특수문자 중 2가지
        val pwPattern2 = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[\$@\$!%*#?&]).{6,16}.\$" // 영문, 숫자, 특수문자 모두

        return (Pattern.matches(pwPattern1, password) ||
                Pattern.matches(pwPattern2, password))
        TODO("비밀번호 확인 전에 비밀번호 누를 때부터 타입 검사해야 하는데 그럼 addtextlistener...? 정했는데 기억 못하는 건가")
    }
}