package com.example.buildupfrontend.record

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.buildupfrontend.databinding.ActivityStep3Binding

class Step3Activity : AppCompatActivity() {
    private lateinit var binding: ActivityStep3Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityStep3Binding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}