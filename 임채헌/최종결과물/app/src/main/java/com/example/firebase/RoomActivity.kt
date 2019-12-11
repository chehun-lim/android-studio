package com.example.firebase

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.firebase.KakaoLink.KakaoLinkProvider.sendKakaoLink
import com.google.firebase.database.*
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import kotlinx.android.synthetic.main.activity_room.*


class RoomActivity : BaseActivity() {

    private lateinit var database: DatabaseReference  //파이어베이스 서버에 데이터를 읽고 쓰기 위한 변수
    private var passwordValue :String ?= ""  //사용자가 들어가려고 하는 방의 비밀번호를 firebase서버에서 읽어와서 저장하는 변수
    private var roomCheck :Int=0  //사용자가 만들려고 하는 방이 이미 있는지를 firebase 서버에서 읽어오는 변수
    private var roomNumberCheck :Int=0 //사용자가 들어가려고 하는 방이 이미 꽉찼는지 안꽉찼는지 여부를 firebase 서버에서 읽어오는 변수

    companion object {
        private const val TAG = "KotlinActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        permissionCheckListener = object : PermissionCheckListener {
            override fun permissionCheckFinish() {
                Log.e("log", "start Activity")
            }
        }

        database = FirebaseDatabase.getInstance().reference  //파이어베이스 서버에 데이터를 읽고 쓰기 위한 변수

        roomName.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { //방이름을 바꿔서 입력할때마다 실행되는 listener
                database.child("rooms").addValueEventListener(object : ValueEventListener { //
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        passwordValue = dataSnapshot.child(roomName.text.toString()).child("password").value.toString()
                        //firebase 서버로부터 해당방의 비밀번호를 가져온다
                        if (dataSnapshot.hasChild(roomName.text.toString())) { //firebase서버에 이미 사용자가 입력한 이름의 방이 있는 경우
                            roomCheck=1
                        }else{ //firebase 서버에 사용자가 입력한 이름의 방이 없는 경우
                            roomCheck=2
                        }

                        if (intent.hasExtra("loginInformation")) { //MainActivity로부터 intent가 넘어왔다면
                            val info = intent.getSerializableExtra("loginInformation") as Information //intent 정보를 info에 저장
                            if (dataSnapshot.child(roomName.text.toString()).hasChild("1") &&
                                dataSnapshot.child(roomName.text.toString()).hasChild("2") &&
                                dataSnapshot.child(roomName.text.toString()).hasChild("3") &&
                                dataSnapshot.child(roomName.text.toString()).child("1").child("username").value.toString() != info.name &&
                                dataSnapshot.child(roomName.text.toString()).child("2").child("username").value.toString() != info.name &&
                                dataSnapshot.child(roomName.text.toString()).child("3").child("username").value.toString() != info.name
                            ) {
                                // 사용자가 입력한방에 이미 사람이 꽉차고 해당방의 서버에 자신이 없는 경우
                                roomNumberCheck = 1
                            } else { //사용자가 입력한방에 아직 들어갈 수 있는 경우
                                roomNumberCheck = 2
                            }
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
                // 공유하기 버튼의 listener
                if (intent.hasExtra("loginInformation")) { //MainActivity로부터 intent가 넘어왔다면
                    if (roomName.text.toString() != "" && password.text.toString()!= "") { //방이름과 비밀번호가 모두 입력된 상태라면
                        val info = intent.getSerializableExtra("loginInformation") as Information //intent 정보를 info에 저장
                        info.roomName = roomName.text.toString() //방이름 정보도 info에 저장

                        if(roomCheck==2) { //사용자가 입력한 방이 기존에 없는 경우
                            val user = miniUser(info.name, info.email) //사용자의 정보를 firebase 서버에 올리기 위한 형태로 저장
                            database.child("rooms").child(roomName.text.toString()).setValue(user) //위의 정보를 가지고 방생성
                            database.child("rooms").child(roomName.text.toString())
                                .child("password").setValue(password.text.toString()) //위에서 생성한 방에 비밀번호도 서버에 저장
                            sendKakaoLink(this, info.roomName) //카카오톡 공유페이지로 넘어가기

                        } else if (roomCheck==1){ //사용자가 입력한 방이 기존에 있는 경우 메시지 출력
                            Toast.makeText(
                                this, "이미 있는 방입니다.",
                                Toast.LENGTH_SHORT
                            ).show()

                        }


                    } else{ //사용자가 방이름이나 비밀번호중 아무것도 입력하지 않은 것이 있을 때
                        Toast.makeText(
                            this, "방이름과 비밀번호를 입력하세요.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }


            map.setOnClickListener {
                //위치 설정하기 버튼 listener
                if (intent.hasExtra("loginInformation")) { //MainActivity로부터 intent가 넘어왔다면
                    if (roomName.text.toString() != "" && password.text.toString()!= "") { //방이름과 비밀번호가 모두 입력된 상태라면
                        if(password.text.toString() == passwordValue  ) { //사용자가 입력한 비밀번호가 서버에 저장된 비밀번호와 일치하면
                            if(roomNumberCheck == 2) { //사용자가 들어가려고 하는 방에 사람이 꽉차지 않은 경우
                                val info = intent.getSerializableExtra("loginInformation") as Information //intent 정보를 info에 저장
                                info.roomName = roomName.text.toString() //방이름 정보도 info에 저장

                                if (permissionCheck( //자신의 위치정보 추적을 위해 권한 허용여부 체크
                                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                                    )
                                ) { //허용이 안되어있다면
                                    ActivityCompat.requestPermissions( //권한허용을 요청
                                        this,
                                        arrayOf(
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION
                                        ),
                                        PERMISSION_REQUEST
                                    )

                                } else { //허용이 되어있다면
                                    permissionCheckListener.permissionCheckFinish()


                                    val intent = Intent(this, MapActivity::class.java)
                                    intent.putExtra("Information", info)
                                    startActivityForResult(intent, 2) //사용자의 정보를 intent에 넣고 MapActivity로 넘어감
                                }
                            } else if (roomNumberCheck==1){ //사용자가 들어가려고 하는 방에 사람이 꽉찬 경우
                                Toast.makeText(
                                    this, "꽉찬 방입니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }else{ //사용자가 들어가려고 하는 방의 비밀번호가 서버에서 불러온 값과 다를 때
                            Toast.makeText(
                                this, "비밀번호가 틀렸습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    } else{ //사용자가 방이름과 비밀번호중 입력하지 않은 것이 있을 때
                        Toast.makeText(
                            this, "방이름과 비밀번호를 입력하세요.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
    }
}
