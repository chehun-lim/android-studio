package com.example.firebase

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat




abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(setLayout)
    }

    val PERMISSION_REQUEST: Int = 1001

    interface PermissionCheckListener {
        fun permissionCheckFinish()
    }

    lateinit var permissionCheckListener: PermissionCheckListener

    fun permissionCheck(strings: Array<String>): Boolean {
        // 변수 사용시 리턴 타입 지정.
        // 변수 사용안하고 바로 리턴시켜 버림.
        //안드로이드 마시멜로 이후 퍼미션 체크.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (i in 0 until strings.size) {
                if (ContextCompat.checkSelfPermission(this, strings[i]) == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, strings, PERMISSION_REQUEST)
                    return true
                }
            }
        }
        return false
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST) {
            //안드로이드 마시멜로 이후 퍼미션 체크
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                for (i in grantResults.indices) {
                    if (grantResults[i] == 0) {
                        if (grantResults.size == i + 1) {
                            permissionCheckListener.permissionCheckFinish()

                        }
                    } else {
                        // 거부한 이력이 있으면 true를 반환
                        if (shouldShowRequestPermissionRationale(permissions[i])) {
                            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST)
                        } else {
                            AlertDialog.Builder(this)
                                .setTitle("권한 설정이 필요합니다!")
                                .setMessage(permissions[i] + " 권한이 거부되었습니다 설정에서 직접 권한을 허용 해주세요.")
                                .setNeutralButton("설정") { dialog, which ->
                                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    intent.data = Uri.parse("package:$packageName")
                                    startActivity(intent)
                                    Toast.makeText(applicationContext, "권한 설정 후 다시 실행 해주세요.", Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                                .setPositiveButton("확인") { dialog, which ->
                                    Toast.makeText(applicationContext, "권한 설정을 하셔야 앱을 사용할 수 있습니다.", Toast.LENGTH_SHORT)
                                        .show()
                                    finish()
                                }

                                .setCancelable(false)
                                .create().show()
                        }// shouldShowRequestPermissionRationale /else
                    } // 권한 거절
                } // for end
            }//Build.VERSION.SDK_INT  end
        }//requestCode  end
    }
}
