package com.example.buildupfrontend.record

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.buildupfrontend.databinding.ActivityStep2Binding

class Step2Activity : AppCompatActivity() {
    private lateinit var binding: ActivityStep2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityStep2Binding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}