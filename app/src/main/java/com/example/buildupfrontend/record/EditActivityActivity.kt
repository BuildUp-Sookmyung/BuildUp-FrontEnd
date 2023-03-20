package com.example.buildupfrontend.record

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.buildupfrontend.R
import com.example.buildupfrontend.databinding.ActivityEditActivityBinding

class EditActivityActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityEditActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}