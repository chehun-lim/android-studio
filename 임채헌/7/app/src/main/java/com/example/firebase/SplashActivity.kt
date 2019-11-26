package com.example.firebase

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    val SPLASH_TIME_OUT: Long = 3000 //3초간 보여 주고 넘어 간다.



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)



        val intent = Intent(this, MainActivity::class.java)            // 실제 사용할 메인 액티비티

        //startActivity(intent)

        //finish()



        Handler().postDelayed({
            //어떤 액티비티로 넘어 갈지 설정 - 당연히 메인액티비로 넘어 가면 된다.
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, SPLASH_TIME_OUT)


    }

}
