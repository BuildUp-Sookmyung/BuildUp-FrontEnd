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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentId.newInstance] factory method to
 * create an instance of this fragment.
 */
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