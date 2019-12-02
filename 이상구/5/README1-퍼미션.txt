
�۹̼� �ڵ� ���� ����� README

-------------------------------------------------------------------------------------------------------------------------



1. manifest <application �±� �ٷ� ���� ���� �۹̼� ����



<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />



-------------------------------------------------------------------------------------------------------------------------



2. app ���� build.gradle�� ���� ���� �ִ��� Ȯ��, ������ �߰�



	implementation 'com.google.android.gms:play-services-maps:17.0.0'
	implementation 'com.google.android.gms:play-services-location:17.0.0'




-------------------------------------------------------------------------------------------------------------------------



3. BaseActivity ����, �Ʒ� �ڵ� ������ ����






package com.swpj.pre1012 //���� �ٲٱ�

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



    abstract val setLayout: Int



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)



        setContentView(setLayout)

    }


    private val PERMISSION_REQUEST: Int = 1001



    interface PermissionCheckListener {

        fun permissionCheckFinish()

    }


    lateinit var permissionCheckListener: PermissionCheckListener



    fun permissionCheck(strings: Array<String>): Boolean {

        // ���� ���� ���� Ÿ�� ����.

        // var check = false



        // ���� �����ϰ� �ٷ� ���Ͻ��� ����.



        //�ȵ���̵� ���ø�� ���� �۹̼� üũ.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            for (i in 0 until strings.size) {

                if (ContextCompat.checkSelfPermission(this, strings[i]) == PackageManager.PERMISSION_DENIED) {

                    ActivityCompat.requestPermissions(this, strings, PERMISSION_REQUEST)

                    // check = true

                    // break

                    return true
                }
            }
        }

        //return check
        return false
    }


    

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {



        super.onRequestPermissionsResult(requestCode, permissions, grantResults)



        if (requestCode == PERMISSION_REQUEST) {

            //�ȵ���̵� ���ø�� ���� �۹̼� üũ

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                for (i in grantResults.indices) {

                    if (grantResults[i] == 0) {

                        if (grantResults.size == i + 1) {

                            permissionCheckListener.permissionCheckFinish()

                        }

                    } else {


                        // �ź��� �̷��� ������ true�� ��ȯ

                        if (shouldShowRequestPermissionRationale(permissions[i])) {

                            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST)


                        } else {


                            AlertDialog.Builder(this)

                                .setTitle("���� ������ �ʿ��մϴ�!")

                                .setMessage(permissions[i] + " ������ �źεǾ����ϴ� �������� ���� ������ ��� ���ּ���.")

                                .setNeutralButton("����") { dialog, which ->

                                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)

                                    intent.data = Uri.parse("package:$packageName")

                                    startActivity(intent)

                                    Toast.makeText(applicationContext, "���� ���� �� �ٽ� ���� ���ּ���.", Toast.LENGTH_SHORT).show()

                                    finish()

                                }

                                .setPositiveButton("Ȯ��") { dialog, which ->

                                    Toast.makeText(applicationContext, "���� ������ �ϼž� ���� ����� �� �ֽ��ϴ�.", Toast.LENGTH_SHORT)

                                        .show()

                                    finish()

                                }

                                .setCancelable(false)

                                .create().show()

                        }// shouldShowRequestPermissionRationale /else

                    } // ���� ����

                } // for end

            }//Build.VERSION.SDK_INT  end

        }//requestCode  end
    }
}



-------------------------------------------------------------------------------------------------------------------------


4. StartActivity ����, �Ʒ� �ڵ� ���� �� ����






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



-------------------------------------------------------------------------------------------------------------------------


