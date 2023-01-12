package com.cookandroid.buildup.record

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cookandroid.buildup.R
import com.cookandroid.buildup.databinding.ActivityStep1Binding

class Step1Activity : AppCompatActivity() {
    private lateinit var binding: ActivityStep1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityStep1Binding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}