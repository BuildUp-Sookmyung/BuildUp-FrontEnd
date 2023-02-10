package com.example.buildupfrontend

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

@AndroidEntryPoint
open class FragmentSU2: FragmentSharedUser() {

    private val viewModel : SignupViewModel by activityViewModels()

    lateinit var tlPw: TextInputLayout
    lateinit var tlPw2: TextInputLayout

    lateinit var etPw: TextInputEditText
    lateinit var etPw2: TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater!!.inflate(R.layout.fragment_su2, container, false)
        (activity as SignupActivity?)!!.setProgressBar(2)
        val userName = viewModel.userName
        initView(mView!!)
        tvTop.text = userName + "님,\n사용하실 계정 정보를 입력해주세요."

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
            } else if (!noduplicateId(checkId)) {
                tlId.error = "* 이미 사용중인 아이디입니다."
            } else {
                tlId.error = null
                tlId.helperText = "* 사용 가능한 아이디입니다."
            }
        }

        // 비밀번호 타입 검사
        tlPw.checkType()

        // 비밀번호, 비밀번호 확인 입력 길이 같으면 '가입완료' 활성화
        etPw2.enableOk()

        // '가입완료' 눌렀을 때 비밀번호 일치 여부 확인
        btnOk.setOnClickListener {
            if (etPw.text.toString() != etPw2.text.toString()) {
                tlPw2.error = "* 비밀번호가 일치하지 않습니다."
            } else {
                nextStep()
            }
        }
        return mView
    }

    override fun initView(view:View) {
        tvTop = view.findViewById<TextView>(R.id.tv_top)
        tlId = view.findViewById<TextInputLayout>(R.id.tl_id)
        tlPw = view.findViewById<TextInputLayout>(R.id.tl_pw)
        tlPw2 = view.findViewById<TextInputLayout>(R.id.tl_pw2)
        etId = view.findViewById<TextInputEditText>(R.id.et_id)
        etPw = view.findViewById<TextInputEditText>(R.id.et_pw)
        etPw2 = view.findViewById<TextInputEditText>(R.id.et_pw2)

        btnVerify = view.findViewById<Button>(R.id.btn_verify)
        btnOk = view.findViewById<Button>(R.id.btn_ok)
        btnVerify.isEnabled = false // 처음에는 '인증 요청' 버튼 활성화 x
        tvTop.visibility = View.VISIBLE

        // onBackPressed()
        etId.setText(viewModel.userID)
        etPw.setText(viewModel.userPW)
        etPw2.setText(viewModel.userPW)
        btnOk.isEnabled = etPw.text == etPw2.text // 처음 온 거면 disabled, 프로필 설정하다가 뒤로 가기 한 거면 enabled
        TODO("SU3에서 backPressed 했을 때 disabled. but text로 보이면 enabled 됨")
    }

    protected fun TextInputLayout.checkType() {
        this.editText?.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
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
    }

    fun TextInputEditText.enableOk() {
        this.addTextChangedListener(object: TextWatcher {
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
    }

    private fun noduplicateId(checkId: String): Boolean {
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

    override fun onBackPressed() {
        (activity as SignupActivity?)!!.nextFragment(1, FragmentSU1())
    }

    override fun nextStep() {
        viewModel.userID = etId.text.toString()
        viewModel.userPW = etPw.text.toString()
        (activity as SignupActivity?)!!.nextFragment(3, FragmentSU3())
        return
    }

}
