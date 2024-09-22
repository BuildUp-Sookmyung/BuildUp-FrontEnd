package com.example.buildupfrontend.record

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil.setContentView
import com.example.buildupfrontend.R
import com.example.buildupfrontend.databinding.DialogCourseRecommendBinding

class CourseRecommendDialog(
    context: Context,
): Dialog(context) {
    private lateinit var binding: DialogCourseRecommendBinding

    init{
        binding=DialogCourseRecommendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgCourseClose.setOnClickListener {
            dismiss()
        }
    }
}