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



class MainActivity : AppCompatActivity(), View.OnClickListener{


    private lateinit var database: DatabaseReference
    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]

    var information = Information()

    private lateinit var googleSignInClient: GoogleSignInClient

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

        if(intent.action == Intent.ACTION_VIEW) {

            /*val boardId = intent.data?.getQueryParameter(roomInfo).toString()


            information.roomName = boardId*/

            //information.roomName = intent.data?.getQueryParameter(roomName).toString()

            //GotoDetailBoard.go(this, boardId) // startActivity to DetailActivity


            signInButton.setOnClickListener {
                signIn()
            }

            signOutButton.setOnClickListener {
                signOut()
            }

            nextButton.setOnClickListener {
                next()

            }



        }else {

            signInButton.setOnClickListener {
                signIn()
            }

            signOutButton.setOnClickListener {
                signOut()
            }

            nextButton.setOnClickListener {
                next()

            }
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



    fun writeNewUser(
        userId: String,
        name: String,
        email: String
    ) {
        database = FirebaseDatabase.getInstance().reference

        val user = User(name, email)
        database.child("users").child(email).setValue(user)
    }





    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)
        // [START_EXCLUDE silent]
        //showProgressDialog()
        // [END_EXCLUDE]

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                    if (user!=null) {
                        /*writeNewUser(
                            user.uid,
                            user.displayName.toString(),
                            user.email.toString()

                        )*/
                        information.userId = user.uid
                        information.name = user.displayName.toString()
                        information.email = user.email.toString()
                        val arr = information.email.split("@")
                        val emailValue = arr[0]
                        database = FirebaseDatabase.getInstance().reference

                        val usera = User(information.name, information.email)
                        database.child("users").child(emailValue).setValue(usera)

                    }
                    /*val intent1 = Intent(this, RoomActivity::class.java)
                    intent1.putExtra("loginInformation",information)
                    startActivityForResult(intent1, 1)*/
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

    private fun next(){
        val user = auth.currentUser

        if (user!=null&&user.email != null && user.displayName != null) {


            /*writeNewUser(
                user.uid,
                user.displayName.toString(),
                user.email.toString()

            )*/

            information.userId = user.uid
            information.name = user.displayName.toString()
            information.email = user.email.toString()
            val arr = information.email.split("@")
            val emailValue = arr[0]
            database = FirebaseDatabase.getInstance().reference

            val usera = User(information.name, information.email)
            database.child("users").child(emailValue).setValue(usera)

                val intent1 = Intent(this, RoomActivity::class.java)
                intent1.putExtra("loginInformation", information)
                startActivityForResult(intent1, 1)

        } else {
            Toast.makeText(
                this, "login first",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun kakaonext(){
        val user = auth.currentUser

        if (user!=null&&user.email != null && user.displayName != null) {
            information.userId = user.uid
            information.name = user.displayName.toString()
            information.email = user.email.toString()

            val intent2 = Intent(this, MapActivity::class.java)
            intent2.putExtra("Information", information)
            startActivityForResult(intent2, 2)
        } else {
            Toast.makeText(
                this, "login first",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun revokeAccess() {
        // Firebase sign out
        auth.signOut()

        // Google revoke access
        googleSignInClient.revokeAccess().addOnCompleteListener(this) {
            updateUI(null)
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        //hideProgressDialog()
        if (user != null) {
            //status.text = getString(R.string.google_status_fmt, user.email)
            //detail.text = getString(R.string.firebase_status_fmt, user.uid)

            signInButton.visibility = View.GONE
            //signOutAndDisconnect.visibility = View.VISIBLE
        } else {
            //status.setText(R.string.signed_out)
            //detail.text = null

            signInButton.visibility = View.VISIBLE
            //signOutAndDisconnect.visibility = View.GONE
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    override fun onClick(v: View) {
        /*val i = v.id
        when (i) {
            R.id.signInButton -> signIn()
            R.id.signOutButton -> signOut()
            R.id.disconnectButton -> revokeAccess()
        }*/
    }





    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }


}
