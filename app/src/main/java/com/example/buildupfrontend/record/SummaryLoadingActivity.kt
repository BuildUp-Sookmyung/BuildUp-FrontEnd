package com.example.buildupfrontend.record

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.content.ContentProviderCompat.requireContext
import com.bumptech.glide.Glide
import com.example.buildupfrontend.R
import com.example.buildupfrontend.databinding.ActivitySummaryLoadingBinding

class SummaryLoadingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySummaryLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySummaryLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Glide.with(this).load(R.drawable.loading_spinner).override(150, 150).into(binding.ivLoadingGif)

        Handler().postDelayed({
            val dialog=CourseRecommendDialog(this)
            dialog.show()
        }, 2000)
    }
}