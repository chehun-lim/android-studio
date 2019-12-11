package com.example.firebase

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class SplashActivity : AppCompatActivity() {
    //앱실행할때 처음 로딩화면을 보여주기 위한 activity

    private val SPLASH_TIME_OUT: Long = 3000 //3초간 보여 주고 넘어 간다.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, MainActivity::class.java)            // 실제 사용할 메인 액티비티

        Handler().postDelayed({
            //어떤 액티비티로 넘어 갈지 설정 - 당연히 메인액티비로 넘어 가면 된다.
            startActivity(intent)
            finish()
        }, SPLASH_TIME_OUT)
    }
}
