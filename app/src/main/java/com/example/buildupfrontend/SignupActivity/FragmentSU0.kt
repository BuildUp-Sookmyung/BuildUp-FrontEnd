package com.example.buildupfrontend.SignupActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.example.buildupfrontend.FragmentSharedUser
import com.example.buildupfrontend.R
import com.example.buildupfrontend.ViewModels.SignupViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentSU0: FragmentSharedUser() {

    private val viewModel : SignupViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        (activity as SignupActivity?)!!.setProgressBar(0)

        return super.getView()
    }

    override fun initView(view:View) {
        tvTop = view.findViewById<TextView>(R.id.tv_top)
        tlName = view.findViewById<TextInputLayout>(R.id.tl_name)
        tlEmail = view.findViewById<TextInputLayout>(R.id.tl_email)
        tlVerify = view.findViewById<TextInputLayout>(R.id.tl_verify)

        etName = view.findViewById<TextInputEditText>(R.id.et_name)
        etEmail = view.findViewById<TextInputEditText>(R.id.et_email)
        etVerify = view.findViewById<TextInputEditText>(R.id.et_verify)

        etName.setText(viewModel.userName)
        etEmail.setText(viewModel.userEmail)

        btnVerify = view.findViewById<Button>(R.id.btn_verify)
        btnOk = view.findViewById<Button>(R.id.btn_ok)
        tvTimer = view.findViewById<TextView>(R.id.tv_timer)

        validName = viewModel.validName
        validEmail = viewModel.validEmail

        btnVerify.isEnabled = validName && validEmail // 처음에는 '인증 요청' 버튼 활성화 x
        btnOk.isEnabled = false
        tlVerify.visibility = View.GONE // 처음에는 '인증번호를 입력하세요'란 안 보임
        tvTop.visibility = View.VISIBLE
    }

    override fun nextStep() {
        viewModel.validName = true
        viewModel.validEmail = true
        viewModel.userName = etName.text.toString()
        viewModel.userEmail = etEmail.text.toString()

        (activity as SignupActivity?)!!.nextFragment(1, FragmentSU1())
    }
}