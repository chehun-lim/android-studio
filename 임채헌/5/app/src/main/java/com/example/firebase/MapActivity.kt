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
import android.widget.*
import androidx.annotation.VisibleForTesting
import com.example.firebase.KakaoLink.KakaoLinkProvider.sendKakaoLink
import com.google.firebase.database.*
import com.kakao.message.template.LinkObject
import com.kakao.message.template.TextTemplate
import kotlinx.android.synthetic.main.activity_map.*

class MapActivity : AppCompatActivity(),  OnMapReadyCallback {

    val TAG = "MyMessage"

    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)



        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }

        mapFragment.getMapAsync(this)






        //lateinit var database: DatabaseReference// ...
        //database = FirebaseDatabase.getInstance().reference


        /*// Write a message to the database
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("message")

        myRef.setValue("hello 야호")

        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue(String::class.java)
                Log.d(TAG, "Value is: $value")

                *//*val accounts = arrayOf(value)
                val list : ListView = findViewById<ListView>(R.id.account_list)

                var adapter =  ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1,accounts)

                list.adapter = adapter*//*
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })*/

    }


    @UiThread
    override fun onMapReady(naverMap: NaverMap) {
        /*val coord = LatLng(37.5670135, 126.9783740)

        Toast.makeText(this,
            "위도: ${coord.latitude}, 경도: ${coord.longitude}",
            Toast.LENGTH_SHORT).show()*/


        /*val marker = Marker().apply {
            position = LatLng(37.5670135, 126.9783740)
            setOnClickListener {
                Toast.makeText(this@MainActivity, "마커 클릭됨", Toast.LENGTH_SHORT).show()
                true
            }
            map = naverMap
        }*/


        val markers = arrayOfNulls<Marker>(4)
        var count = 0
        var sndpoint: Int = 0
        var endpoint: LatLng = LatLng(0.000000, 0.000000)
        var markerend: Marker = Marker()

        val markerdest = Marker()
        markerdest.position = LatLng(37.5670135, 126.9783740)
        markers[3] = markerdest

        fun destination(marker1: Marker?, marker2: Marker?, marker3: Marker?, marker4: Marker?) {
            if (marker1 != null && marker2 != null && marker3 != null && marker4 != null && count > 2) {
                var latitudes =
                    (marker1.position.latitude + marker2.position.latitude + marker3.position.latitude) / 3
                var longitudes =
                    (marker1.position.longitude + marker2.position.longitude + marker3.position.longitude) / 3
                marker4.position = LatLng(latitudes, longitudes)
                marker4.map = naverMap
            }
        }

        fun reset(marker1: Marker?, marker2: Marker?, marker3: Marker?, marker4: Marker?) {

            marker1?.map = null
            marker2?.map = null
            marker3?.map = null
            marker4?.map = null

        }


        database = FirebaseDatabase.getInstance().reference




        fun writeNewUser(
            userId: String,
            name: String,
            email: String,
            latitude: Double,
            longitude: Double
        ) {
            val user = User(name, email, latitude, longitude)
            database.child("rooms").child("no room").push().setValue(user)
        }



        /*naverMap.setOnMapClickListener { point, coord ->
            if (count < 3) {

                Toast.makeText(
                    this, "${coord.latitude}, ${coord.longitude}",
                    Toast.LENGTH_SHORT
                ).show()
                var marker = Marker()
                marker.position = LatLng(coord.latitude, coord.longitude)
                marker.map = naverMap
                markers[count] = marker
                count++
                if (intent.hasExtra("Information")) {
                    val info = intent.getSerializableExtra("Information") as Information
                    if (info.name != null) {
                        val user = User(info.name, info.email, coord.latitude, coord.longitude)
                        database.child("rooms").child(info.roomName).setValue(user)

                    }

                    //Toast.makeText(this, info.email, Toast.LENGTH_SHORT).show()

                }
            } else {
                Toast.makeText(
                    this, "${coord.latitude}, ${coord.longitude}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }*/


        fun getMarker(roomInfo: String){
            val datas : DatabaseReference = database.child("rooms/").child(roomInfo).ref
            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get Post object and use the values to update the UI
                    val post = dataSnapshot.getValue(User::class.java)
                    // [START_EXCLUDE]

                    var otherMarker = Marker()
                    if (post!= null && post.latitude != null) {
                        //otherMarker.position = LatLng(post.latitude, post.longitude)
                        otherMarker.map = naverMap
                    }

                    // [END_EXCLUDE]
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                    // [START_EXCLUDE]
                    Toast.makeText(
                        baseContext, "Failed to load post.",
                        Toast.LENGTH_SHORT
                    ).show()
                    // [END_EXCLUDE]
                }
            }
            datas.addValueEventListener(postListener)
        }

        /*final private DatabaseReference userRef = mDatabase.child("users").child("premium").child("normal").getRef();

//...

        public void getUser(String uID){

            //Call our reference
            userRef.child(uID).addListenerForSingleValueEvent(
                new ValueEventListener () {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // ...
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                    }
                });
        }*/

        naverMap.setOnMapClickListener { point, coord ->

            markers[count]?.map = null

                Toast.makeText(
                    this, "${coord.latitude}, ${coord.longitude}",
                    Toast.LENGTH_SHORT
                ).show()
                var marker = Marker()
                marker.position = LatLng(coord.latitude, coord.longitude)
                marker.map = naverMap
                markers[count] = marker
                //count++
                /*if (intent.hasExtra("Information")) {
                    val info = intent.getSerializableExtra("Information") as Information
                    if (info.name != null) {
                        val user = User(info.name, info.email, coord.latitude, coord.longitude)
                        database.child("rooms").child(info.roomName).setValue(user)

                    }

                    //Toast.makeText(this, info.email, Toast.LENGTH_SHORT).show()

                }*/

        }

        fun findwayfir(markeruser: Marker?): Int { //주어진 좌표와 가장 가까운 거점을 반환

            //val markerarr = arrayOfNulls<LatLng>(13)
            //val markerarrlat = [LatLng(37.534599, 126.981566),LatLng(38.179464, 128.542655),LatLng(37.885245, 127.735159),LatLng(37.452607, 126.696952),LatLng(37.452607, 126.696952),LatLng(36.332371, 127.394583),LatLng(36.623890, 127.493460),LatLng(35.164363, 129.048026),LatLng(35.155382, 126.828787),LatLng(34.804317, 126.394827),LatLng(35.826261, 128.564627),LatLng(35.812898, 129.262259),LatLng(35.540704, 129.256766),LatLng(35.200281, 128.130667)]
            val markerarrlat: Array<Double> = arrayOf( //전국 주요지점 좌표
                37.534599,
                38.179464,
                37.885245,
                37.452607,
                37.452607,
                36.332371,
                36.623890,
                35.164363,
                35.155382,
                34.804317,
                35.826261,
                35.812898,
                35.540704,
                35.200281
            )
            val markerarrlon: Array<Double> = arrayOf(
                126.981566,
                128.542655,
                127.735159,
                126.696952,
                126.839774,
                127.394583,
                127.493460,
                129.048026,
                126.828787,
                126.394827,
                128.564627,
                129.262259,
                129.256766,
                128.130667
            )

            /*markerarr[0] = LatLng(37.534599, 126.981566)
            markerarr[1] = LatLng(38.179464, 128.542655)
            markerarr[2] = LatLng(37.885245, 127.735159)
            markerarr[3] = LatLng(37.452607, 126.696952)
            markerarr[4] = LatLng(37.452607, 126.696952)
            markerarr[5] = LatLng(36.332371, 127.394583)
            markerarr[6] = LatLng(36.623890, 127.493460)
            markerarr[7] = LatLng(35.164363, 129.048026)
            markerarr[8] = LatLng(35.155382, 126.828787)
            markerarr[9] = LatLng(34.804317, 126.394827)
            markerarr[10] = LatLng(35.826261, 128.564627)
            markerarr[11] = LatLng(35.812898, 129.262259)
            markerarr[12] = LatLng(35.540704, 129.256766)
            markerarr[13] = LatLng(35.200281, 128.130667)*/

            //아래부턴 두 지점의 거리 제곱 구해 저장하는 코드
            var i = 0
            val temArrlat: Array<Double> = arrayOf(
                0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00
            )
            val temArrlon: Array<Double> = arrayOf(
                0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00
            )
            val temArrsum: Array<Double> = arrayOf(
                0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00
            )

            val numtem: Array<Int> = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13)


            for (index in 0..13 step 1) {
                if (markeruser != null) {
                    temArrlat[i] = Math.pow(markeruser.position.latitude - markerarrlat[i], 2.00)
                    temArrlon[i] = Math.pow(markeruser.position.longitude - markerarrlon[i], 2.00)
                    if (temArrsum[i] != null && temArrlat[i] != null && temArrlon[i] != null) {
                        temArrsum[i] = temArrlat[i] + temArrlon[i]
                    }
                } else {
                    break
                }
                i++
            }
            //아래부턴 저장된 값중 가장 작은값을 구하는 코드
            var j = 0
            var k = 0
            for (index in 0..12 step 1) {
                if (temArrsum[j] < temArrsum[j + 1]) {
                    temArrsum[j + 1] = temArrsum[j]
                    numtem[k + 1] = numtem[k]
                }
                j++
                k++
            }
            var rest = numtem[13] //가장 거리가 작은 값의 위치
            return rest

        }

        fun resetOne(marker: Marker?) {
            if (marker != null) {
                marker.map = null
            } else {
                marker?.map = null
            }
        }


        fun findwaysnd(lNguser: Marker?): String { //주어진 거점과 가장 가까운 지점 지명을 반환


            val markerarrlats: Array<Double> = arrayOf( //서울 주요지점 좌표
                37.534659,
                37.556830,
                37.556003,
                37.540218,
                37.497299,
                37.570125,
                37.505441,
                37.504493,
                37.582312,
                37.573341,
                37.613127,
                37.580563,
                37.535720,
                37.543971
            )
            val markerarrlons: Array<Double> = arrayOf(
                126.993941,
                126.924531,
                126.936845,
                127.068996,
                127.026681,
                126.991239,
                127.004708,
                127.082889,
                126.983575,
                126.976816,
                127.030211,
                127.002058,
                126.875263,
                127.036595
            )

            //아래부턴 두 지점의 거리 제곱 구해 저장하는 코드
            var i = 0
            val temArrlat: Array<Double> = arrayOf(
                0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00
            )
            val temArrlon: Array<Double> = arrayOf(
                0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00
            )
            val temArrsum: Array<Double> = arrayOf(
                0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00
            )
            val numtem: Array<Int> = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13)

            for (index in 0..13 step 1) {
                if (lNguser != null) {
                    temArrlat[i] = Math.pow(lNguser.position.latitude - markerarrlats[i], 2.00)
                    temArrlon[i] = Math.pow(lNguser.position.longitude - markerarrlons[i], 2.00)
                    if (temArrsum[i] != null && temArrlat[i] != null && temArrlon[i] != null) {
                        temArrsum[i] = temArrlat[i] + temArrlon[i]
                    }
                } else {
                    break
                }
                i++
            }
            //아래부턴 저장된 값중 가장 작은값을 구하는 코드
            var j = 0
            var k = 0
            for (index in 0..12 step 1) {
                if (temArrsum[j] < temArrsum[j + 1]) {
                    temArrsum[j + 1] = temArrsum[j]
                    numtem[k + 1] = numtem[k]
                }
                j++
                k++
            }
            var rest = numtem[13] //가장 거리가 작은 값의 위치

            //이하는 최종 추천장소 반환, endpoint에 좌표 저장
            if (rest == 0) {
                endpoint = LatLng(markerarrlats[0], markerarrlons[0])
                return "이태원"
            } else if (rest == 1) {
                endpoint = LatLng(markerarrlats[1], markerarrlons[1])
                return "홍대입구"
            } else if (rest == 2) {
                endpoint = LatLng(markerarrlats[2], markerarrlons[2])
                return "신촌"
            } else if (rest == 3) {
                endpoint = LatLng(markerarrlats[3], markerarrlons[3])
                return "건대입구"
            } else if (rest == 4) {
                endpoint = LatLng(markerarrlats[4], markerarrlons[4])
                return "강남역"
            } else if (rest == 5) {
                endpoint = LatLng(markerarrlats[5], markerarrlons[5])
                return "종로"
            } else if (rest == 6) {
                endpoint = LatLng(markerarrlats[6], markerarrlons[6])
                return "고속버스터미널"
            } else if (rest == 7) {
                endpoint = LatLng(markerarrlats[7], markerarrlons[7])
                return "잠실"
            } else if (rest == 8) {
                endpoint = LatLng(markerarrlats[8], markerarrlons[8])
                return "북촌한옥마을"
            } else if (rest == 9) {
                endpoint = LatLng(markerarrlats[9], markerarrlons[9])
                return "광화문"
            } else if (rest == 10) {
                endpoint = LatLng(markerarrlats[10], markerarrlons[10])
                return "미아사거리"
            } else if (rest == 11) {
                endpoint = LatLng(markerarrlats[11], markerarrlons[11])
                return "대학로"
            } else if (rest == 12) {
                endpoint = LatLng(markerarrlats[12], markerarrlons[12])
                return "목동"
            } else if (rest == 13) {
                endpoint = LatLng(markerarrlats[13], markerarrlons[13])
                return "서울숲"
            }
            return "없음"

        }


        /*naverMap.setOnMapClickListener { point, coord ->
            if(count<3) {
                Toast.makeText(this, "${coord.latitude}, ${coord.longitude}",
                    Toast.LENGTH_SHORT).show()
                var marker = Marker()
                marker.position = LatLng(coord.latitude, coord.longitude)
                marker.map = naverMap
                markers[count] = marker
                count++
            }
            else{
                Toast.makeText(this, "${coord.latitude}, ${coord.longitude}",
                    Toast.LENGTH_SHORT).show()
            }
        }*/


        var oNe = findViewById<Button>(R.id.button1)

        button1.setOnClickListener {
            /*//첫번째버튼 누를시
            destination(markers[0], markers[1], markers[2], markers[3]) //1차 최단거리장소
            var savepoint = findwaysnd(markers[3]) //최종 추천장소 이름
            Toast.makeText(
                this, "${savepoint}",
                Toast.LENGTH_SHORT
            ).show()
            var marker = Marker()
            marker.position = endpoint //endpoint가 최종 추천장소
            marker.map = naverMap
            markerend = marker
            if (intent.hasExtra("Information")) {
                val info = intent.getSerializableExtra("Information") as Information
                if (info.name != null) {
                    val user = User(info.name, info.email, endpoint.latitude, endpoint.longitude)
                    database.child("rooms").child(info.roomName).child(info.name).setValue(user)
                }

                //Toast.makeText(this, info.email, Toast.LENGTH_SHORT).show()

            }*/
            if(intent.hasExtra("Information")) {
                val info = intent.getSerializableExtra("Information") as Information
                getMarker(info.roomName)
            }

        }


        var tWo = findViewById<Button>(R.id.button3)

        button3.setOnClickListener {
            //리셋버튼
            reset(markers[0], markers[1], markers[2], markers[3])
            count = 0
            resetOne(markerend)

            if (intent.hasExtra("Information")) {
                val info = intent.getSerializableExtra("Information") as Information
                if (info.name != null) {
                    database.child("rooms").child(info.roomName).child(info.name).child("latitude").removeValue()
                    database.child("rooms").child(info.roomName).child(info.name).child("longitude").removeValue()
                }

                //Toast.makeText(this, info.email, Toast.LENGTH_SHORT).show()

            }

        }

        var num=0

        if (intent.hasExtra("Information")) {
            val info = intent.getSerializableExtra("Information") as Information
            database.child("rooms").child(info.roomName)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        //val value = dataSnapshot.getValue(String::class.java)
                        if (dataSnapshot.hasChild("1") && dataSnapshot.hasChild("2") && dataSnapshot.child("2").child("email").value != info.email && dataSnapshot.child("1").child("email").value != info.email) {
                            num=3 //3번에 들어가는 코드
                        } else if(dataSnapshot.hasChild("1") && dataSnapshot.child("1").child("email").value != info.email) {
                            num=2 //2번에 들어가는 코드
                        } else {
                            num=1 //1번에 들어가는 코드
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Getting Post failed, log a message
                        Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                        // ...
                    }
                })
        }


        button2.setOnClickListener {

            if (intent.hasExtra("Information")) {
                val info = intent.getSerializableExtra("Information") as Information
                if (info.name != null && markers[count]!=null ) {
                    val arr = info.email.split("@")
                    val emailValue = arr[0]
                    val user = User(info.name, info.email, markers[count]?.position?.latitude, markers[count]?.position?.longitude)
                    //database.child("rooms").child(info.roomName).child("사용자").child(emailValue).setValue(user)


                            //val value = dataSnapshot.getValue(String::class.java)
                            if(num==2) {
                                val user = User(info.name, info.email, markers[count]?.position?.latitude, markers[count]?.position?.longitude)
                                database.child("rooms").child(info.roomName).child("2").setValue(user)
                            }else if(num==1){
                                val user = User(info.name, info.email, markers[count]?.position?.latitude, markers[count]?.position?.longitude)
                                database.child("rooms").child(info.roomName).child("1").setValue(user)
                            }else if(num==3){
                                val user = User(info.name, info.email, markers[count]?.position?.latitude, markers[count]?.position?.longitude)
                                database.child("rooms").child(info.roomName).child("3").setValue(user)
                            }






                    /*if (num==1){
                        database.child("rooms").child(info.roomName).child("1").setValue(user)
                    }else if(num==2){
                        database.child("rooms").child(info.roomName).child("2").setValue(user)
                        }*/

                }

                //Toast.makeText(this, info.email, Toast.LENGTH_SHORT).show()

            }
        }




        var g:Double? = 0.00
        var h:Double? = 0.00
        var i:Double? = 0.00
        var j:Double? = 0.00
        var k:Double? = 0.00
        var l:Double? = 0.00

        val markerarr = arrayOfNulls<Marker>(3)

        test.setOnClickListener {
            if (intent.hasExtra("Information")) {

                val info = intent.getSerializableExtra("Information") as Information
                val c = info.roomName
                database.child("rooms").child(c).child("1")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            /*fileList.clear()
                            for (postSnapshot in dataSnapshot.children) {
                                // TODO: handle the post
                                val value = postSnapshot.getValue(User::class.java)
                                if(value!=null) {
                                    fileList.add(value)
                                }
                            }*/
                            val value = dataSnapshot.getValue(User::class.java)
                            if(value!=null) {
                                g = value.latitude
                                h = value.longitude
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Getting Post failed, log a message
                            Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                            // ...
                        }
                    })



                database.child("rooms").child(c).child("2")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            /*fileList.clear()
                            for (postSnapshot in dataSnapshot.children) {
                                // TODO: handle the post
                                val value = postSnapshot.getValue(User::class.java)
                                if(value!=null) {
                                    fileList.add(value)
                                }
                            }*/
                            val value = dataSnapshot.getValue(User::class.java)
                            if(value!=null) {
                                i = value.latitude
                                j = value.longitude
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Getting Post failed, log a message
                            Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                            // ...
                        }
                    })


                database.child("rooms").child(c).child("3")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            /*fileList.clear()
                            for (postSnapshot in dataSnapshot.children) {
                                // TODO: handle the post
                                val value = postSnapshot.getValue(User::class.java)
                                if(value!=null) {
                                    fileList.add(value)
                                }
                            }*/
                            val value = dataSnapshot.getValue(User::class.java)
                            if(value!=null) {
                                k = value.latitude
                                l = value.longitude
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Getting Post failed, log a message
                            Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                            // ...
                        }
                    })


                val marker1 = Marker()
                val marker2 = Marker()
                val marker3 = Marker()


                marker1.position = LatLng(g!!,h!!)
                marker1.map = naverMap
                markerarr[0] = marker1

                marker2.position = LatLng(i!!,j!!)
                marker2.map = naverMap
                markerarr[1] = marker2

                marker3.position = LatLng(k!!,l!!)
                marker3.map = naverMap
                markerarr[2] = marker3





            }
        }


    }
}


