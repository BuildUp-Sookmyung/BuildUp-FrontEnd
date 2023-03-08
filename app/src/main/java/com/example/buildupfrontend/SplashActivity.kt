package com.example.buildupfrontend

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val content = findViewById<View>(android.R.id.content)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            content.viewTreeObserver.addOnDrawListener { false }
        }
        callNextScreen()


    }

    private fun callNextScreen() {
        // 일정 시간 지연 이후 실행하기 위한 코드
        Handler(Looper.getMainLooper()).postDelayed({

            // 일정 시간이 지나면 LoginActivity로 이동
            val intent= Intent( this,LoginActivity::class.java)
            startActivity(intent)

            // 이전 키를 눌렀을 때 스플래스 스크린 화면으로 이동을 방지하기 위해
            // 이동한 다음 사용안함으로 finish 처리
            finish()

        }, 2000) // 시간 2000초 이후 실행
    }
}