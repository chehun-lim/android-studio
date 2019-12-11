package com.example.firebase


import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.UiThread
import androidx.fragment.app.FragmentActivity
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import kotlinx.android.synthetic.main.activity_main.*
import androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior.setTag
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.location.Address
import android.location.Geocoder
import android.os.Looper
import android.widget.*
import androidx.annotation.VisibleForTesting
import androidx.core.app.ActivityCompat
import com.example.firebase.KakaoLink.KakaoLinkProvider.sendKakaoLink
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.*
import com.kakao.message.template.LinkObject
import com.kakao.message.template.TextTemplate
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Align
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.MarkerIcons
import kotlinx.android.synthetic.main.activity_map.*
import java.io.IOException

class MapActivity : AppCompatActivity(),  OnMapReadyCallback {

    val TAG = "MyMessage"
    private lateinit var database: DatabaseReference  //파이어베이스 서버에 데이터를 읽고 쓰기 위한 변수
    var userNum=0 //사용자를 해당 방에 들어왔던 순서대로 1,2,3을 부여해서 사용자끼리 서로 구별하기 위한 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(naverMap: NaverMap) { //MapActivity가 실행되고 지도화면이 나왔을 때

        val markers = arrayOfNulls<Marker>(4) //배열 4짜리로 저장한 후 사용자들의 좌표정보를 저장하는 변수
        //markers[0]은 자신의 좌표값 markers[1]과 markers[2]는 다른 사용자의 좌표값 markers[3]은 중간지점을 저장한다
        var endpoint : LatLng = LatLng(0.000000,0.000000) //최종 추천지점 좌표

        var geocLatLng : LatLng = LatLng(0.000,0.000)
        var geocstat : Int = 0 //불러온 정보가 유효하면 1, 아니면 0

        val markerEnd = Marker() //중간지점에서 가장 가까운 최종 추천 지점을 나타내는 마커
        var clickMarker = Marker() //지도를 클릭했을 때 나타나는 마커
        clickMarker.position = LatLng(0.00, 0.00) //초기화

        var markerdest = Marker() //중간지점을 저장하는 마커
        markerdest.position = LatLng(37.5670135, 126.9783740)
        markers[3] = markerdest

        var fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val currentMarker = Marker() //현재위치를 나타내는 마커
        database = FirebaseDatabase.getInstance().reference //파이어베이스 서버에 데이터를 읽고 쓰기 위한 변수

        var roomm = "" // RoomActivity에서 넘어온 방이름 정보를 저장하기 위한 변수

        var rest : Int = 0

        fun initLocation() { //현재위치정보를 나타내기 위해 권한을 체크하는 함수
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return
            }

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if(location == null) {
                        Log.e(TAG, "location get fail")
                    } else {
                        Log.d(TAG, "${location.latitude} , ${location.longitude}")
                    }
                }
                .addOnFailureListener {
                    Log.e(TAG, "location error is ${it.message}")
                    it.printStackTrace()
                }
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
                    temArrsum[i] = temArrlat[i] + temArrlon[i]
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
            val rest1 = numtem[13] //가장 거리가 작은 값의 위치
            return rest1

        }

        fun resetOne(marker: Marker?) {
            //marker를 지도에서 안보이게 하는 함수
            if (marker != null) {
                marker.map = null
            } else {
                marker?.map = null
            }
        }


        fun findwaysnd(lNguser: Marker?): String { //주어진 거점과 가장 가까운 지점 지명을 반환
            val markerarrlats: Array<Double> = arrayOf( //서울 주요지점 좌표
                37.534659,37.556830,37.556003,37.540218,37.497299,37.570125,37.505441,37.504493,37.582312,37.573341,37.613127,37.580563,37.535720,37.543971,37.533909, 37.514056, 37.529860, 37.481193, 37.479380, 37.517362, 37.581168, 37.592541, 37.578304, 37.571166
            )
            val markerarrlons: Array<Double> = arrayOf(
                126.993941,126.924531,126.936845,127.068996,127.026681,126.991239,127.004708,127.082889,126.983575,126.976816,127.030211,127.002058,126.875263,127.036595,126.901894, 126.941258, 126.964552, 126.952573, 127.011797, 127.041040, 127.048615, 127.016308, 126.900013, 127.009152
            )

            //아래부턴 두 지점의 거리 제곱 구해 저장하는 코드
            var i = 0
            val temArrlat: Array<Double> = arrayOf(
                0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00
            )
            val temArrlon: Array<Double> = arrayOf(
                0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00
            )
            val temArrsum: Array<Double> = arrayOf(
                0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00
            )
            val numtem: Array<Int> = arrayOf(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15)

            for (index in 0..15 step 1) {
                if (lNguser != null) {
                    temArrlat[i] = Math.pow(lNguser.position.latitude - markerarrlats[i], 2.00)
                    temArrlon[i] = Math.pow(lNguser.position.longitude - markerarrlons[i], 2.00)
                    temArrsum[i] = temArrlat[i] + temArrlon[i]

                } else {
                    break
                }
                i++
            }
            //아래부턴 저장된 값중 가장 작은값을 구하는 코드
            var j = 0
            var k = 0
            for (index in 0..14 step 1) {
                if (temArrsum[j] < temArrsum[j + 1]) {
                    temArrsum[j + 1] = temArrsum[j]
                    numtem[k + 1] = numtem[k]
                }
                j++
                k++
            }
            val rest = numtem[15] //가장 거리가 작은 값의 위치

            //이하는 최종 추천장소 반환, endpoint에 좌표 저장
            if (rest == 0) {
                endpoint = LatLng(markerarrlats[0], markerarrlons[0])
                return "이태원"
            }
            else if (rest == 1) {
                endpoint = LatLng(markerarrlats[1], markerarrlons[1])
                return "홍대입구"
            }
            else if (rest == 2) {
                endpoint = LatLng(markerarrlats[2], markerarrlons[2])
                return "신촌"
            }
            else if (rest == 3) {
                endpoint = LatLng(markerarrlats[3], markerarrlons[3])
                return "건대입구"
            }
            else if (rest == 4) {
                endpoint = LatLng(markerarrlats[4], markerarrlons[4])
                return "강남역"
            }
            else if (rest == 5) {
                endpoint = LatLng(markerarrlats[5], markerarrlons[5])
                return "종로"
            }
            else if (rest == 6) {
                endpoint = LatLng(markerarrlats[6], markerarrlons[6])
                return "고속버스터미널"
            }
            else if (rest == 7) {
                endpoint = LatLng(markerarrlats[7], markerarrlons[7])
                return "잠실"
            }
            else if (rest == 8) {
                endpoint = LatLng(markerarrlats[8], markerarrlons[8])
                return "북촌한옥마을"
            }
            else if (rest == 9) {
                endpoint = LatLng(markerarrlats[9], markerarrlons[9])
                return "광화문"
            }
            else if (rest == 10) {
                endpoint = LatLng(markerarrlats[10], markerarrlons[10])
                return "미아사거리"
            }
            else if (rest == 11) {
                endpoint = LatLng(markerarrlats[11], markerarrlons[11])
                return "대학로(혜화)"
            }
            else if (rest == 12) {
                endpoint = LatLng(markerarrlats[12], markerarrlons[12])
                return "목동"
            }
            else if (rest == 13) {
                endpoint = LatLng(markerarrlats[13], markerarrlons[13])
                return "서울숲"
            }
            else if (rest == 14) {
                endpoint = LatLng(markerarrlats[14], markerarrlons[14])
                return "당산"
            }
            else if (rest == 15) {
                endpoint = LatLng(markerarrlats[15], markerarrlons[15])
                return "노량진"
            }

            return "없음"

        }

        fun destination(marker1: Marker?, marker2: Marker?, marker3: Marker?, marker4: Marker?) {
            //나와 다른 사람들의 좌표값들을 토대로 중간지점을 구하는 함수 중간지점은 marker4에 저장
            if (marker1 != null && marker2 != null && marker3 != null && marker4 != null ) { //나를 포함한 사용자가 3명일때
                val latitudes =
                    (marker1.position.latitude + marker2.position.latitude + marker3.position.latitude) / 3
                val longitudes =
                    (marker1.position.longitude + marker2.position.longitude + marker3.position.longitude) / 3
                marker4.position = LatLng(latitudes, longitudes) //중간지점 좌표 저장
                marker4.map = naverMap //중간지점 지도에 표시
                markerdest = marker4 //중간지점 저장

                val savepoint = findwaysnd(marker4) //최종 추천장소 이름
                Toast.makeText(
                    this, "${savepoint}",
                    Toast.LENGTH_SHORT
                ).show()
                markerEnd.icon = MarkerIcons.BLACK
                markerEnd.iconTintColor = Color.CYAN
                markerEnd.captionText = "최종추천"
                markerEnd.setCaptionAligns(Align.Top)
                markerEnd.position = endpoint //endpoint가 최종 추천장소
                markerEnd.map = naverMap //최종 추천 장소 지도에 표시
                if (intent.hasExtra("Information")) {
                    //RoomActivity로부터 intent를 가져와서 유저정보로 최종 추천 장소의 좌표값을 서버에 저장
                    val info = intent.getSerializableExtra("Information") as Information
                    val user = User(info.name, info.email, endpoint.latitude, endpoint.longitude)
                    database.child("rooms").child(info.roomName).child(info.name).setValue(user)
                }
            }else if(marker1 != null && marker2 != null && marker3 == null && marker4 != null){ //나를 포함한 사용자가 2명일 때
                val latitudes =
                    (marker1.position.latitude + marker2.position.latitude) / 2
                val longitudes =
                    (marker1.position.longitude + marker2.position.longitude) / 2
                marker4.position = LatLng(latitudes, longitudes) //중간지점 좌표 저장
                marker4.map = naverMap //중간지점 지도에 표시
                markerdest = marker4 //중간지점 저장

                val savepoint = findwaysnd(marker4) //최종 추천장소 이름
                Toast.makeText(
                    this, "${savepoint}",
                    Toast.LENGTH_SHORT
                ).show()
                markerEnd.icon = MarkerIcons.BLACK
                markerEnd.iconTintColor = Color.CYAN
                markerEnd.captionText = "최종추천"
                markerEnd.setCaptionAligns(Align.Top)
                markerEnd.position = endpoint //endpoint가 최종 추천장소
                markerEnd.map = naverMap //최종추천장소 지도에 표시
                if (intent.hasExtra("Information")) {
                    //RoomActivity로부터 intent를 가져와서 유저정보로 최종 추천 장소의 좌표값을 서버에 저장
                    val info = intent.getSerializableExtra("Information") as Information
                    val user = User(info.name, info.email, endpoint.latitude, endpoint.longitude)
                    database.child("rooms").child(info.roomName).child(info.name).setValue(user)
                }
            } else{
                Toast.makeText(
                    this, "다른 사람이 위치를 지정할 때까지 기다려주세요",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        //지오코딩 함수 - 좌표 넣으면 주소로 변환
        fun revGeoc(repoint:LatLng = LatLng(37.600114, 126.864815)){

            val mGeoCoder : Geocoder = Geocoder(this)

            var mResultList : List<Address>? = null

            mResultList = mGeoCoder.getFromLocation(
                repoint.latitude,
                repoint.longitude,
                10
            )

            var geocodedad : String = "a"
            geocodedad = mResultList[0].toString()

            val str1 : Int = geocodedad.indexOf(":") +2
            val geostr1 : String = geocodedad.substring(str1 , 100)
            val str2 : Int = geostr1.indexOf("]") +24
            var geostr : String = "a"
            geostr = geocodedad.substring(str1 , str2)

            Toast.makeText(
                this, geostr,
                Toast.LENGTH_SHORT
            ).show()

        }

        //지오코딩 함수 - 주소 넣으면 좌표 변환
        fun GeocF(addr : String = "한국항공대학교"){
            val mGeoCoder : Geocoder = Geocoder(this)

            var resultLocation: List<Address>? = null

            try {
                resultLocation = mGeoCoder.getFromLocationName(addr, 1)

            }
            catch (e: IOException){
                e.printStackTrace()
                Log.d(TAG, "주소변환 실패")
            }

            if (resultLocation != null) {
                if (resultLocation.size !=0) { //유효한 주소를 입력했을 때
                    val resltLat: Double = resultLocation.get(0).latitude
                    val resltLng: Double = resultLocation.get(0).longitude
                    geocLatLng = LatLng(resltLat, resltLng)
                    geocstat = 1
                }
                else{ //유효한 주소가 아닐 때
                    //texv.setText("해당되는 주소 정보는 없습니다")
                    geocstat = 0
                }

            }


        }

        val infoWindow = InfoWindow() //마커 정보창을 띄우기 위한 객체 생성

        naverMap.setOnMapClickListener { point, coord ->

            // 지도를 클릭했을 때 그 지점에 마커를 찍는다
            clickMarker.map = null

            Toast.makeText(
                this, "${coord.latitude}, ${coord.longitude}",
                Toast.LENGTH_SHORT
            ).show()
            val marker = Marker()
            marker.icon = MarkerIcons.BLACK
            marker.iconTintColor = Color.YELLOW
            marker.captionText = "나"
            marker.setCaptionAligns(Align.Top)
            marker.position = LatLng(coord.latitude, coord.longitude)
            revGeoc(marker.position)
            clickMarker = marker
            clickMarker.map = naverMap
            resetOne(currentMarker) //현재위치는 지도에서 사라지게
        }

        infoWindow.close()  //마커 밖의 맵 클릭시 마커 정보창 닫음

        // MapActivity가 열릴 때 한번만 실행해서
        // RoomActivity로부터 넘어온 사용자 정보를 가지고 방에 들어온 순서와
        // 다른 사용자가 몇번으로 부여받았는지를 고려해서 각 유저에게 중복되지 않게 1,2,3중에서 번호 부여
        if (intent.hasExtra("Information")) {
            val info = intent.getSerializableExtra("Information") as Information
            roomm = info.roomName
            database.child("rooms").child(info.roomName)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        //val value = dataSnapshot.getValue(String::class.java)
                        if (dataSnapshot.hasChild("1") && dataSnapshot.hasChild("2") && dataSnapshot.child(
                                "2"
                            ).child("email").value != info.email && dataSnapshot.child("1").child("email").value != info.email
                        ) {
                            userNum = 3 //3번에 들어가는 코드
                            val user = User(info.name, info.email)
                            database.child("rooms").child(info.roomName).child("3").setValue(user)
                        } else if (dataSnapshot.hasChild("1") && dataSnapshot.child("1").child("email").value != info.email) {
                            userNum = 2 //2번에 들어가는 코드
                            val user = User(info.name, info.email)
                            database.child("rooms").child(info.roomName).child("2").setValue(user)
                        } else {
                            userNum = 1 //1번에 들어가는 코드
                            val user = User(info.name, info.email)
                            database.child("rooms").child(info.roomName).child("1").setValue(user)
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Getting Post failed, log a message
                        Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                        // ...
                    }
                })
        }

        val marker1 = Marker() //자신을 제외한 다른 사용자 1
        marker1.icon = MarkerIcons.BLACK
        marker1.iconTintColor = Color.RED
        marker1.setCaptionAligns(Align.Top)

        val marker2 = Marker()  //자신을 제외한 다른 사용자 2
        marker2.icon = MarkerIcons.BLACK
        marker2.iconTintColor = Color.BLUE
        marker2.setCaptionAligns(Align.Top)

        //firebase 서버로부터 내가 들어온 방에 있는 다른 사람들의 좌표값을 실시간으로 불러와서 지도에 마커로 찍어준다
        //나의 번호와 다른 사람이 1명 있는지 2명 있는지를 고려해서 찍는다
        database.child("rooms").child(roomm)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    if(userNum==1) {
                        if (dataSnapshot.hasChild("2") && dataSnapshot.hasChild("3")) {
                            marker1.captionText = dataSnapshot.child("2").child("username").value.toString()
                            val value1 = dataSnapshot.child("2").child("latitude").value
                            val value2 = dataSnapshot.child("2").child("longitude").value
                            marker1.position = LatLng(value1.toString().toDouble(), value2.toString().toDouble())

                            marker2.captionText = dataSnapshot.child("3").child("username").value.toString()
                            val value3 = dataSnapshot.child("3").child("latitude").value
                            val value4 = dataSnapshot.child("3").child("longitude").value
                            marker2.position = LatLng(value3.toString().toDouble(), value4.toString().toDouble())

                            marker1.map = naverMap
                            markers[1] = marker1

                            marker2.map = naverMap
                            markers[2] = marker2
                        } else if(dataSnapshot.hasChild("2")){
                            marker1.captionText = dataSnapshot.child("2").child("username").value.toString()
                            val value1 = dataSnapshot.child("2").child("latitude").value
                            val value2 = dataSnapshot.child("2").child("longitude").value
                            marker1.position = LatLng(value1.toString().toDouble(), value2.toString().toDouble())

                            marker1.map = naverMap
                            markers[1] = marker1
                            markers[2] = null
                        } else if(dataSnapshot.hasChild("3")){
                            marker1.captionText = dataSnapshot.child("3").child("username").value.toString()
                            val value1 = dataSnapshot.child("3").child("latitude").value
                            val value2 = dataSnapshot.child("3").child("longitude").value
                            marker1.position = LatLng(value1.toString().toDouble(), value2.toString().toDouble())

                            marker1.map = naverMap
                            markers[1] = marker1
                            markers[2] = null
                        }
                    } else if(userNum==2){
                        if (dataSnapshot.hasChild("1") && dataSnapshot.hasChild("3")) {
                            marker1.captionText = dataSnapshot.child("1").child("username").value.toString()
                            val value1 = dataSnapshot.child("1").child("latitude").value
                            val value2 = dataSnapshot.child("1").child("longitude").value
                            marker1.position = LatLng(value1.toString().toDouble(), value2.toString().toDouble())

                            marker2.captionText = dataSnapshot.child("3").child("username").value.toString()
                            val value3 = dataSnapshot.child("3").child("latitude").value
                            val value4 = dataSnapshot.child("3").child("longitude").value
                            marker2.position = LatLng(value3.toString().toDouble(), value4.toString().toDouble())

                            marker1.map = naverMap
                            markers[1] = marker1

                            marker2.map = naverMap
                            markers[2] = marker2
                        } else if(dataSnapshot.hasChild("1")){
                            marker1.captionText = dataSnapshot.child("1").child("username").value.toString()
                            val value1 = dataSnapshot.child("1").child("latitude").value
                            val value2 = dataSnapshot.child("1").child("longitude").value
                            marker1.position = LatLng(value1.toString().toDouble(), value2.toString().toDouble())

                            marker1.map = naverMap
                            markers[1] = marker1
                            markers[2] = null
                        } else if(dataSnapshot.hasChild("3")){
                            marker1.captionText = dataSnapshot.child("3").child("username").value.toString()
                            val value1 = dataSnapshot.child("3").child("latitude").value
                            val value2 = dataSnapshot.child("3").child("longitude").value
                            marker1.position = LatLng(value1.toString().toDouble(), value2.toString().toDouble())

                            marker1.map = naverMap
                            markers[1] = marker1
                            markers[2] = null
                        }

                    } else if(userNum==3) {
                        if (dataSnapshot.hasChild("1") && dataSnapshot.hasChild("2")) {
                            marker1.captionText = dataSnapshot.child("1").child("username").value.toString()
                            val value1 = dataSnapshot.child("1").child("latitude").value
                            val value2 = dataSnapshot.child("1").child("longitude").value
                            marker1.position = LatLng(value1.toString().toDouble(), value2.toString().toDouble())

                            marker2.captionText = dataSnapshot.child("2").child("username").value.toString()
                            val value3 = dataSnapshot.child("2").child("latitude").value
                            val value4 = dataSnapshot.child("2").child("longitude").value
                            marker2.position = LatLng(value3.toString().toDouble(), value4.toString().toDouble())

                            marker1.map = naverMap
                            markers[1] = marker1

                            marker2.map = naverMap
                            markers[2] = marker2
                        } else if (dataSnapshot.hasChild("1")) {
                            marker1.captionText = dataSnapshot.child("1").child("username").value.toString()
                            val value1 = dataSnapshot.child("1").child("latitude").value
                            val value2 = dataSnapshot.child("1").child("longitude").value
                            marker1.position = LatLng(value1.toString().toDouble(), value2.toString().toDouble())

                            marker1.map = naverMap
                            markers[1] = marker1
                            markers[2] = null
                        } else if (dataSnapshot.hasChild("2")) {
                            marker1.captionText = dataSnapshot.child("2").child("username").value.toString()
                            val value1 = dataSnapshot.child("2").child("latitude").value
                            val value2 = dataSnapshot.child("2").child("longitude").value
                            marker1.position = LatLng(value1.toString().toDouble(), value2.toString().toDouble())

                            marker1.map = naverMap
                            markers[1] = marker1
                            markers[2] = null
                        }
                    }
                }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Getting Post failed, log a message
                            Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                            // ...
                        }
                    })

        recommendButton.setOnClickListener {
            //장소추천버튼
            if(markers[0]?.map==null || markers[0]?.position?.latitude ==0.00){
                //내가 출발지 설정을 안했을 때
                Toast.makeText(
                    this, "자신의 출발지를 설정해주세요",
                    Toast.LENGTH_SHORT
                ).show()
            } else if(markers[1]?.position?.latitude!=0.00 && markers[2]?.position?.latitude!=0.00) {
                //나를 포함한 이 방의 모든 사용자가 서버에 출발지를 등록했을 때
                destination(markers[0], markers[1], markers[2], markers[3]) //1차 최단거리장소

                val savepoint = findwaysnd(markerdest)

                infoWindow.adapter = object : InfoWindow.DefaultTextAdapter(this) {
                    override fun getText(infoWindow: InfoWindow): CharSequence {
                        return savepoint.toString()
                    }
                }


                markerEnd.tag = "마커 1" // 마커 클릭을 위한 객체 생성
                markerEnd.setOnClickListener { //생성된 마커 클릭시 리스너
                    infoWindow.open(markerEnd) //마커 클릭 시 정보창 띄움
                    true

                }
                markerEnd.setOnClickListener { //생성된 마커 클릭시 리스너2
                    if (savepoint == "이태원") { //마커 클릭 시 rest(최종 위치가 저장된 변수)에 따라 fragment를 불러오는 부분
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragview, ResultFrag()) //fragview 부분을 해당 fragment로 바꿈
                            .commit() //위의 변경사항 적용
                    }
                    else if (savepoint == "홍대입구") {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragview, ResultFrag2())
                            .commit()
                    }
                    else if (savepoint == "신촌") {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragview, ResultFrag3())
                            .commit()
                    }
                    else if (savepoint == "건대입구") {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragview, ResultFrag4())
                            .commit()
                    }
                    else if (savepoint == "강남역") {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragview, ResultFrag5())
                            .commit()
                    }
                    else if (savepoint == "종로") {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragview, ResultFrag6())
                            .commit()
                    }
                    else if (savepoint == "고속버스터미널") {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragview, ResultFrag7())
                            .commit()
                    }
                    else if (savepoint == "잠실") {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragview, ResultFrag8())
                            .commit()
                    }
                    else if (savepoint == "북촌한옥마을") {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragview, ResultFrag9())
                            .commit()
                    }
                    else if (savepoint == "광화문") {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragview, ResultFrag10())
                            .commit()
                    }
                    else if (savepoint == "미아사거리") {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragview, ResultFrag11())
                            .commit()
                    }
                    else if (savepoint == "대학로(혜화)") {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragview, ResultFrag12())
                            .commit()
                    }
                    else if (savepoint == "목동") {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragview, ResultFrag13())
                            .commit()
                    }
                    else if (savepoint == "서울숲") {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragview, ResultFrag14())
                            .commit()
                    }
                    else if (savepoint == "당산") {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragview, ResultFrag15())
                            .commit()
                    }
                    else if (savepoint == "노량진") {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragview, ResultFrag16())
                            .commit()
                    }


                    true

                }


            } else {
                //다른 사람이 출발지 설정으로 서버에 위치를 등록하지 않았을 때
                Toast.makeText(
                    this, "다른 사람이 위치를 지정할 때까지 기다려주세요",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }


        resetButton.setOnClickListener {
            //다시하기버튼

            resetOne(markerEnd) //최종추전장소 사라지게
            resetOne(markers[3])  //중간지점 사라지게
            resetOne(clickMarker)  //지도에클릭한마커 사라지게
            resetOne(currentMarker)  //현재위치 사라지게

            //서버에 저장했던 자신의 출발지 설정위치를 0으로 초기화
            if (intent.hasExtra("Information")) {
                val info = intent.getSerializableExtra("Information") as Information

                database.child("rooms").child(info.roomName).child(userNum.toString()).child("latitude").setValue("0")
                database.child("rooms").child(info.roomName).child(userNum.toString()).child("longitude").setValue("0")
            }
        }

        locationRegister.setOnClickListener {
            //출발지설정버튼

            //유저정보를 intent로 받아와서 지도에 클릭한 점을 유저별로 firebase 서버에 저장
            if (intent.hasExtra("Information")) {
                val info = intent.getSerializableExtra("Information") as Information
                if(userNum==2) {
                    val user = User(info.name, info.email, clickMarker?.position?.latitude, clickMarker?.position?.longitude)
                    database.child("rooms").child(info.roomName).child("2").setValue(user)
                    markers[0] = clickMarker
                }else if(userNum==1){
                    val user = User(info.name, info.email, clickMarker?.position?.latitude, clickMarker?.position?.longitude)
                    database.child("rooms").child(info.roomName).child("1").setValue(user)
                    markers[0] = clickMarker
                }else if(userNum==3){
                    val user = User(info.name, info.email, clickMarker?.position?.latitude, clickMarker?.position?.longitude)
                    database.child("rooms").child(info.roomName).child("3").setValue(user)
                    markers[0] = clickMarker
                }
            }
        }


        searchButton.setOnClickListener { //위치를 검색한 후 그곳으로 카메라 이동 버튼
            val CamGo = searchpo.text.toString() //검색한 단어를 저장
            GeocF(CamGo) //검색한 단어를 좌표로 변환

            if(geocstat == 1) { //유효한 주소를 입력했을 때
                try {
                    val cameraUpdate = CameraUpdate.scrollTo(geocLatLng)
                        .animate(CameraAnimation.Easing, 1200)
                    naverMap.moveCamera(cameraUpdate) //카메라 이동
                } catch (e: IOException) {
                    e.printStackTrace()
                    Log.d(TAG, "올바른 주소를 입력해주세요")
                }
            }
            else if(geocstat ==0){ //유효한 주소가 아닐 때
                Toast.makeText(
                    this, "해당되는 주소 정보는 없습니다",
                    Toast.LENGTH_SHORT).show()
            }

        }


        //위치 추적
        //자기 자신의 위치를 추적한다.
        initLocation()

        val locationRequest = LocationRequest.create()
        locationRequest.run {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 60 * 1000
        }

        val locationCallback = object: LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult?.let {
                    for((i, location) in it.locations.withIndex()) {
                        Log.d(TAG, "#$i ${location.latitude} , ${location.longitude}")

                        currentMarker.position = LatLng(location.latitude, location.longitude)
                        currentMarker.icon = OverlayImage.fromResource(R.drawable.dddd)
                        currentMarker.width = 160
                        currentMarker.height =160
                    }
                }
            }
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())



        buttongps.setOnClickListener {
            //현재위치로 가면서 그 좌표를 출발지로 설정하는 버튼
            Toast.makeText(
                this, "현재 위치로 출발지를 설정합니다.",
                Toast.LENGTH_SHORT
            ).show()

            val cameraUpdate = CameraUpdate.scrollTo(LatLng(currentMarker.position.latitude, currentMarker.position.longitude))
                .animate(CameraAnimation.Easing, 1200)
            naverMap.moveCamera(cameraUpdate) //현재위치로 카메라 이동
            currentMarker.map = naverMap //현재위치를 지도에 표시
            resetOne(clickMarker) //현재위치를 표시할때 지도를 클릭해서 생긴 마커는 제거
            if (intent.hasExtra("Information")) {
                val info = intent.getSerializableExtra("Information") as Information

                //각 사용자에 따라 서버에 현재위치정보를 저장하고 markers[0]에도 저장
                if(userNum==2) {
                    val user = User(info.name, info.email, currentMarker.position.latitude, currentMarker.position.longitude)
                    database.child("rooms").child(info.roomName).child("2").setValue(user)
                    markers[0] = currentMarker
                }else if(userNum==1){
                    val user = User(info.name, info.email, currentMarker.position.latitude, currentMarker.position.longitude)
                    database.child("rooms").child(info.roomName).child("1").setValue(user)
                    markers[0] = currentMarker
                }else if(userNum==3){
                    val user = User(info.name, info.email, currentMarker.position.latitude, currentMarker.position.longitude)
                    database.child("rooms").child(info.roomName).child("3").setValue(user)
                    markers[0] = currentMarker
                }
            }
        }
    }

    override fun onBackPressed() { //뒤로가기 버튼을 눌렀을 때
        super.onBackPressed()

        if (intent.hasExtra("Information")) { //넘어온 intent가 있으면
            val info = intent.getSerializableExtra("Information") as Information //info에 넘어온 intent를 저장하고
            database.child("rooms").child(info.roomName).child(userNum.toString()).removeValue() //자신의 정보를 서버에서 지운다
        }

    }
}


