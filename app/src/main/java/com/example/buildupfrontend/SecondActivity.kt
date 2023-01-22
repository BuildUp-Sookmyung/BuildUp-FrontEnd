package com.example.buildupfrontend

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.navercorp.nid.oauth.view.NidOAuthLoginButton.Companion.TAG

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        val email = intent.getStringExtra("email")
        val mobile = intent.getStringExtra("mobile")
        val name = intent.getStringExtra("name")
        val birthYear = intent.getStringExtra("birthYear")

        Log.e(TAG, "Info: $name $birthYear $mobile $email")
    }
}