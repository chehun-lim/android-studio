package com.example.firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.example.firebase.KakaoLink.KakaoLinkProvider.sendKakaoLink
import com.google.firebase.database.*
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import kotlinx.android.synthetic.main.activity_room.*


class RoomActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private var passwordValue :String ?= ""
    private var roomCheck :Int=0

    companion object {
        private const val TAG = "KotlinActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        database = FirebaseDatabase.getInstance().reference

        roomName.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                database.child("rooms").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        passwordValue = dataSnapshot.child(roomName.text.toString()).child("password").value.toString()
                        if (dataSnapshot.hasChild(roomName.text.toString())) {
                            roomCheck=1
                        }else{
                            roomCheck=2
                        }


                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException())
                    }
                })

            }
        })


            share.setOnClickListener {
                if (intent.hasExtra("loginInformation")) {
                    if (roomName.text.toString() != "" && password.text.toString()!= "") {
                        val info = intent.getSerializableExtra("loginInformation") as Information
                        info.roomName = roomName.text.toString()

                        val user = miniUser(info.name, info.email)


                        if(roomCheck==2) {

                            database.child("rooms").child(roomName.text.toString()).setValue(user)
                            database.child("rooms").child(roomName.text.toString())
                                .child("password").setValue(password.text.toString())


                            sendKakaoLink(this, info.roomName)

                        } else if (roomCheck==1){
                            Toast.makeText(
                                this, "이미 있는 방입니다.",
                                Toast.LENGTH_SHORT
                            ).show()

                        }


                    } else{
                        Toast.makeText(
                            this, "Enter roomname! and Password!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }


            map.setOnClickListener {
                if (intent.hasExtra("loginInformation")) {
                    if (roomName.text.toString() != "" && password.text.toString()!= "") {

                        if(password.text.toString() == passwordValue  ) {
                            val info =
                                intent.getSerializableExtra("loginInformation") as Information
                            info.roomName = roomName.text.toString()

                            val intent2 = Intent(this, MapActivity::class.java)
                            intent2.putExtra("Information", info)
                            startActivityForResult(intent2, 2)
                        }else{
                            Toast.makeText(
                                this, "Wrong Password!",
                                Toast.LENGTH_SHORT
                            ).show()

                        }


                    } else{
                        Toast.makeText(
                            this, "Enter roomname! and Password!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }

            }


    }
}
