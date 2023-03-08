package com.example.buildupfrontend.FindaccountActivity

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.FragmentManager
import com.example.buildupfrontend.R
import com.example.buildupfrontend.SignupActivity.FragmentSU0
import com.example.buildupfrontend.SignupActivity.SignupActivity
import com.example.buildupfrontend.onBackPressedListener

class FragmentFindID2 : Fragment(), onBackPressedListener {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View = inflater!!.inflate(R.layout.fragment_find_id2, container, false)
        /**
         * 아이디 찾기 성공
         */
        val btnOk = view.findViewById<AppCompatButton>(R.id.btn_login)
        btnOk.setOnClickListener {
            (activity as FindaccountActivity?)!!.loginActivity()
        }

        return view
    }

    //override 해서 사용해주면 된다!
    override fun onBackPressed() {
        this.activity?.finish()
    }

}