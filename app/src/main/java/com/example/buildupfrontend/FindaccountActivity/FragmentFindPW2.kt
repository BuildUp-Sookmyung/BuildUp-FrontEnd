package com.example.buildupfrontend.FindaccountActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.buildupfrontend.R
import com.example.buildupfrontend.SignupActivity.FragmentSU1
import com.example.buildupfrontend.SignupActivity.FragmentSU2
import com.example.buildupfrontend.SignupActivity.SignupActivity
import com.example.buildupfrontend.ViewModels.SignupViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class FragmentFindPW2 : FragmentSU2() {

    private val viewModel : SignupViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater!!.inflate(R.layout.fragment_find_pw2, container, false)
        initView(mView!!)

        // 비밀번호 타입 검사
        tlPw.checkType()

        // 비밀번호, 비밀번호 확인 입력 길이 같으면 '가입완료' 활성화
        etPw2.enableOk()

        // '설정완료' 눌렀을 때 비밀번호 일치 여부 확인
        btnOk.setOnClickListener {
            val checkPw = etPw.text.toString()
            val checkPw2 = etPw2.text.toString()
            if (checkPw != checkPw2) {
                tlPw2.error = "* 비밀번호가 일치하지 않습니다."
            } else {
                viewModel.userPW = checkPw
                nextStep()
            }
        }

        return mView
    }

    override fun initView(view:View) {
        tlPw = view.findViewById<TextInputLayout>(R.id.tl_pw)
        tlPw2 = view.findViewById<TextInputLayout>(R.id.tl_pw2)
        etPw = view.findViewById<TextInputEditText>(R.id.et_pw)
        etPw2 = view.findViewById<TextInputEditText>(R.id.et_pw2)

        btnOk = view.findViewById<Button>(R.id.btn_ok)
        btnOk.isEnabled = false
    }

    override fun onBackPressed() {
        (activity as FindaccountActivity?)!!.nextFragment(FragmentFindPW())
    }

    override fun nextStep() {
        Toast.makeText(context, "비밀번호 재설정이 완료되었습니다.", Toast.LENGTH_SHORT).show()
    }
}