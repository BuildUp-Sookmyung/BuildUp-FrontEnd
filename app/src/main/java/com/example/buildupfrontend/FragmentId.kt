package com.example.buildupfrontend

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.buildupfrontend.databinding.FragmentIdBinding

class FragmentId : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentIdBinding.inflate(inflater, container, false)
        val view: View = binding.root

        binding.btnVerify.setOnClickListener {
            val mobile: String = binding.btnVerify.text.toString()
            sendSMS(mobile)
        }

        return view
    }

    private fun sendSMS(mobile: String) {

    }

}