package com.example.buildupfrontend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.buildupfrontend.databinding.ActivityFindaccountBinding
import com.example.buildupfrontend.databinding.ActivitySignupBinding

class FindaccountActivity : AppCompatActivity() {
    private var mBinding: ActivityFindaccountBinding?=null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityFindaccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFrag(0)

        binding.btnId.setOnClickListener{
            setFrag(0)
        }

        binding.btnPw.setOnClickListener{
            setFrag(1)
        }
    }

    private fun setFrag(fragNum: Int) {
        val ft = supportFragmentManager.beginTransaction()
        when(fragNum) {
            0 -> {
                ft.replace(R.id.main_frame, FragmentID()).commit()
            }
            1 -> {
                ft.replace(R.id.main_frame, FragmentPW()).commit()
            }
        }

    }
}