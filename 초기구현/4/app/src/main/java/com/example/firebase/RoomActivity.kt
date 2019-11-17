package com.example.firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.firebase.KakaoLink.KakaoLinkProvider.sendKakaoLink
import com.google.firebase.database.*
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import kotlinx.android.synthetic.main.activity_room.*


class RoomActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    var a :String ?= ""
    var b :Int=0
    var fileList = ArrayList<String>()

    //private val info by lazy { intent.getSerializableExtra("loginInformation") as Information}
    //private val info = intent.getSerializableExtra("loginInformation") as Information

    /*fun writeNewRoom(
        userId: String,
        name: String,
        email: String,
        roomName : String,
        latitude: Double = 0.00,
        longitude: Double = 0.00
    ) {
        //var roomName : String = email.toString() + "room"
        database = FirebaseDatabase.getInstance().reference
        val user = User(name, email, latitude, longitude,roomName)
        database.child("rooms").setValue(roomName)
        database.child("rooms").push().setValue(user)
    }*/


    companion object {
        private const val TAG = "KotlinActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)



        database = FirebaseDatabase.getInstance().reference

        //val info = intent.getSerializableExtra("loginInformation") as Information


            share.setOnClickListener {
                if (intent.hasExtra("loginInformation")) {
                    if (roomName.text.toString() != "" && password.text.toString()!= "") {
                        val info = intent.getSerializableExtra("loginInformation") as Information
                        info.roomName = roomName.text.toString()

                        val user = User(info.name, info.email)

                        /*database.child("rooms").addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                // This method is called once with the initial value and again
                                // whenever data at this location is updated.
                                val value = dataSnapshot.getValue(String::class.java)
                                if (value!= null) {
                                    fileList.add(value)
                                }

                                Log.d(TAG, "Value is: $value")
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Failed to read value
                                Log.w(TAG, "Failed to read value.", error.toException())
                            }
                        })

                        for(i in 0..fileList.size){
                            if (fileList[i]== roomName.text.toString()){
                                b=1
                            }
                        }*/

                        if(b==0) {

                            database.child("rooms").child(roomName.text.toString()).setValue(user)
                            database.child("rooms").child(roomName.text.toString())
                                .child("password").setValue(password.text.toString())

                            sendKakaoLink(this, info.roomName)
                            /*val intent = Intent(this, MainActivity::class.java);
                        intent.putExtra("imageUri", roomInfo);
                        startActivityForResult(intent, 3);*/
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


                        val datas = FirebaseDatabase.getInstance().reference
                        //val datas = database.child("rooms/").child(roomName.text.toString())




                        database.child("rooms").child(roomName.text.toString()).child("password").addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                // This method is called once with the initial value and again
                                // whenever data at this location is updated.
                                val value = dataSnapshot.getValue(String::class.java)
                                a = value

                                Log.d(TAG, "Value is: $value")
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Failed to read value
                                Log.w(TAG, "Failed to read value.", error.toException())
                            }
                        })





                        if(password.text.toString() == a  ) {
                            val info =
                                intent.getSerializableExtra("loginInformation") as Information
                            info.roomName = roomName.text.toString()

                            val user = User(info.name, info.email)

                            //database.child("rooms").child(roomName.text.toString()).setValue(user)

                            /*val intent = Intent(this, MainActivity::class.java);
                        intent.putExtra("imageUri", roomInfo);
                        startActivityForResult(intent, 3);*/

                            val intent2 = Intent(this, MapActivity::class.java)
                            intent2.putExtra("Information", info)
                            startActivityForResult(intent2, 2)
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
