package com.example.buildupfrontend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.activityViewModels
import com.example.buildupfrontend.databinding.ActivityFindaccountBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FragmentFindID : FragmentSharedUser() {

    private val viewModel : SignupViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        return super.getView()
    }
    override fun nextStep() {
        viewModel.userName = etName.text.toString()
        viewModel.userBday = etBday.text.toString()
        viewModel.userNumber = etNumber.text.toString()
        viewModel.userMobile = etMobile.text.toString()

        viewModel.userID = findID()

        (activity as FindaccountActivity?)!!.nextFragment(FragmentFindID2())
    }

    private fun findID() : String {
        return "내 아이디"
        TODO("아이디 찾기기")
    }
}