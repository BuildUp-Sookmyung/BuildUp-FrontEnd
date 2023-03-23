package com.example.buildupfrontend.FindaccountActivity

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.example.buildupfrontend.R
import com.example.buildupfrontend.SignupActivity.FragmentSU0
import com.example.buildupfrontend.SignupActivity.SignupActivity
import com.example.buildupfrontend.ViewModels.SignupViewModel
import com.example.buildupfrontend.onBackPressedListener

class FragmentFindID2 : Fragment(), onBackPressedListener {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View = inflater!!.inflate(R.layout.fragment_find_id2, container, false)
        val viewModel : SignupViewModel by activityViewModels()
        /**
         * 아이디 찾기 성공
         */
        val btnOk = view.findViewById<AppCompatButton>(R.id.btn_login)
        val tvId = view.findViewById<TextView>(R.id.tv_id)
        val tvDate = view.findViewById<TextView>(R.id.tv_date)

        tvId.text = viewModel.userID
        tvDate.text = viewModel.createdDate

        btnOk.setOnClickListener {
            (activity as FindaccountActivity?)!!.loginActivity()
        }

        return view
    }

    override fun onBackPressed() {
        this.activity?.finish()
    }

}