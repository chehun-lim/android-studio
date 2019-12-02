package com.swpj.pre1012

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log

class StartActivity : BaseActivity() {



    override val setLayout = R.layout.activity_main



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)



        permissionCheckListener = object : PermissionCheckListener {

            override fun permissionCheckFinish() {

                Log.e("log", "start Activity")

            }

        }



        if (permissionCheck(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))) {

            return

        }

        permissionCheckListener.permissionCheckFinish()

        startActivity(Intent(this, SplashActivity::class.java))

    }

}

