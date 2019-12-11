package com.example.firebase


import android.content.Intent
import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.UiThread
import androidx.fragment.app.FragmentActivity
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.e
import com.naver.maps.map.overlay.Marker
import kotlinx.android.synthetic.main.activity_main.*
import androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior.setTag
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.View
import android.widget.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
import com.kakao.network.ErrorResult

class MainActivity : AppCompatActivity(){

    private lateinit var database: DatabaseReference //파이어베이스 서버에 데이터를 읽고 쓰기 위한 변수
    private lateinit var auth: FirebaseAuth  //구글로그인한 사용자의 정보를 파이어베이스에 저장하기 위한 변수
    private var information = Information()  //사용자의 구글로그인정보를 intent로 다음 activity에 넘기기 위한 객체
    private lateinit var googleSignInClient: GoogleSignInClient  //구글로그인한 사용자의 정보를 저장하기 위한 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // [START config_signin]
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        // [END config_signin]

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        // [END initialize_auth]

        signInButton.setOnClickListener {
            //로그인 버튼
            signIn()
        }

        signOutButton.setOnClickListener {
            //로그아웃 버튼
            signOut()
        }

        nextButton.setOnClickListener {
            next()
            //다음 activity로 넘어가는 버튼
        }

    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                // ...
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                    if (user!=null) {
                        information.userId = user.uid
                        information.name = user.displayName.toString()
                        information.email = user.email.toString() //사용자의 정보를 information 객체에 저장
                        val arr = information.email.split("@")
                        val emailValue = arr[0]  //사용자의 정보중 email에서 @앞부분을 저장
                        database = FirebaseDatabase.getInstance().reference  //파이어베이스 서버에 데이터를 읽고 쓰기 위한 변수

                        val miniuser = miniUser(information.name, information.email)  //firebase서버에 저장하기 위한 형태로 유저정보를 변수에 저장
                        database.child("users").child(emailValue).setValue(miniuser) //firebase 서버에 user 정보를 저장

                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, "Authentication Failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }

                // [START_EXCLUDE]
                //hideProgressDialog()
                // [END_EXCLUDE]
            }
    }
    // [END auth_with_google]

    // [START signin]
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    // [END signin]

    private fun signOut() {
        // Firebase sign out
        auth.signOut()

        // Google sign out
        googleSignInClient.signOut().addOnCompleteListener(this) {
            updateUI(null)
        }
    }

    private fun next(){ //next 버튼을 눌렀을 때 실행되는 함수
        val user = auth.currentUser //현재 사용자의 정보를 불러옴

        if (user!=null&&user.email != null && user.displayName != null) { //로그인을 했을때의 조건

            information.userId = user.uid
            information.name = user.displayName.toString()
            information.email = user.email.toString()  //information 객체에 현재 사용자의 정보를 저장
            val arr = information.email.split("@")
            val emailValue = arr[0] //사용자의 정보중 email에서 @앞부분을 저장
            database = FirebaseDatabase.getInstance().reference  //파이어베이스 서버에 데이터를 읽고 쓰기 위한 변수

            val miniuser = miniUser(information.name, information.email)  //firebase서버에 저장하기 위한 형태로 유저정보를 변수에 저장
            database.child("users").child(emailValue).setValue(miniuser)

            val intent = Intent(this, RoomActivity::class.java) //다음 activity인 RoomActivity에 보낼 intent 생성
            intent.putExtra("loginInformation", information) //intent에 사용자의 정보를 저장했던 information 객체를 넣는다
            startActivityForResult(intent, 1) //RoomActivity로 넘어감

        } else {
            Toast.makeText( //로그인 하지 않아서 user정보가 없다면 로그인하라는 메시지 출력
                this, "먼저 로그인 하세요.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun updateUI(user: FirebaseUser?) { //로그인을 했다면 로그인 버튼이 사라지게 로그인을 안했다면 로그인 버튼이 생기게 ui를 바꿔주는 함수
        if (user != null) { //로그인 했다면 로그인 버튼 사라지게
            signInButton.visibility = View.GONE
        } else {  //로그인을 안했다면 로그인 버튼이 생기게
            signInButton.visibility = View.VISIBLE
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }


}
