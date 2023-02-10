package com.example.buildupfrontend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
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
        tlBday = view.findViewById<TextInputLayout>(R.id.tl_bday)
        tlNumber = view.findViewById<TextInputLayout>(R.id.tl_number)
        tlMobile = view.findViewById<TextInputLayout>(R.id.tl_mobile)
        tlVerify = view.findViewById<TextInputLayout>(R.id.tl_verify)

        etName = view.findViewById<TextInputEditText>(R.id.et_name)
        etBday = view.findViewById<TextInputEditText>(R.id.et_bday)
        etNumber = view.findViewById<TextInputEditText>(R.id.et_number)
        etMobile = view.findViewById<TextInputEditText>(R.id.et_mobile)
        etVerify = view.findViewById<TextInputEditText>(R.id.et_verify)

        etName.setText(viewModel.userName)
        etBday.setText(viewModel.userBday)
        etNumber.setText(viewModel.userNumber)

        btnVerify = view.findViewById<Button>(R.id.btn_verify)
        btnOk = view.findViewById<Button>(R.id.btn_ok)
        tvTimer = view.findViewById<TextView>(R.id.tv_timer)

        btnVerify.isEnabled = false // 처음에는 '인증 요청' 버튼 활성화 x
        btnOk.isEnabled = false
        tlVerify.visibility = View.GONE // 처음에는 '인증번호를 입력하세요'란 안 보임
        tvTop.visibility = View.VISIBLE
    }

    override fun nextStep() {
        viewModel.userName = etName.text.toString()
        viewModel.userBday = etBday.text.toString()
        viewModel.userNumber = etNumber.text.toString()
        viewModel.userMobile = etMobile.text.toString()

        (activity as SignupActivity?)!!.nextFragment(1, FragmentSU1())
    }
}