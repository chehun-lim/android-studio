package com.swpj.pre1012

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.fragment.app.FragmentActivity
import com.google.firebase.database.FirebaseDatabase
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import kotlinx.android.synthetic.main.activity_map.*
import android.graphics.Point
import kotlinx.android.synthetic.main.activity_main.*
import androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior.setTag
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import android.widget.*
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.*
import com.kakao.push.StringSet.priority
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Align
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import kotlinx.android.synthetic.main.activity_map.*
import org.naver.Map
import org.naver.Naver
import java.io.IOException
import java.util.*
import com.swpj.pre1012.BaseActivity


/*
val naver = Naver(clientId = "d9qj359o1u", clientSecret = "psvoilLoR8XETF3fLTEFKumSMwhh08BAubKmFnmR")

*/


class MapActivity : AppCompatActivity(), OnMapReadyCallback {

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
    }






    /*class LocationTrackingActivity : AppCompatActivity(), OnMapReadyCallback {
        private lateinit var locationSource: FusedLocationSource

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            // ...
            locationSource =
                FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        }

        override fun onRequestPermissionsResult(requestCode: Int,
                                                permissions: Array<String>,
                                                grantResults: IntArray) {
            if (locationSource.onRequestPermissionsResult(requestCode, permissions,
                    grantResults)) {
                return
            }
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }

        override fun onMapReady(naverMap: NaverMap) {
            naverMap.locationSource = locationSource
        }

        companion object {
            private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        }
    }*/





    /*override fun onResume() {
        super.onResume()
        if (requestingLocationUpdates) startLocationUpdates()
    }

    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest,
            locationCallback,
            null *//* Looper *//*)
    }*/















    @UiThread
    override fun onMapReady(naverMap: NaverMap) {

        /*
        val markerarr = arrayOfNulls<LatLng>(14)
        markerarr[0] = LatLng(37.534599, 126.981566)
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
        markerarr[13] = LatLng(35.200281, 128.130667)
        */


        /*var locationManager : LocationManager? = null
        val REQUEST_CODE_LOCATION : Int = 1000
        var cur_latitude : Double? = null
        var cur_longitude : Double? = null
*/

        var inr = 0
        var latCur : Double = 0.00
        var lonCur : Double = 0.00
        var fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        val markers = arrayOfNulls<Marker>(4)  //찍은 지점 저장되는 마커 배열
        var count = 0
        var endpoint : LatLng = LatLng(0.000000,0.000000) //최종 추천지점 좌표
        var markerend : Marker = Marker()

        var geocLatLng : LatLng = LatLng(0.000,0.000)
        var geocstat : Int = 0 //불러온 정보가 유효하면 1, 아니면 0

        var revgeopo : String = "항공대"

        var centpo : LatLng = LatLng(0.00,0.00) //무게중심




        val markerdest = Marker()

        markerdest.position = LatLng(37.5670135, 126.9783740)
        markers[3] = markerdest






        fun initLocation() {
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


















        fun destination(marker1:Marker?, marker2:Marker?, marker3:Marker?, marker4:Marker?){
            if(marker1!=null&&marker2!=null&&marker3!=null&&marker4!=null) {
                var latitudes =
                    (marker1.position.latitude + marker2.position.latitude + marker3.position.latitude) / 3
                var longitudes =
                    (marker1.position.longitude + marker2.position.longitude + marker3.position.longitude) / 3
                marker4.position = LatLng(latitudes, longitudes)
                marker4.map = naverMap
            }
        }  //여기까지 3개점 무게중심 구하는 함수


        //최대 6개까지 점 받아 무게중심 구하는 함수 -> 변수 centpo 에 좌표 저장
        fun centgrapoint(latlan0:LatLng?,latlan1:LatLng?,latlan2:LatLng?,latlan3:LatLng?,latlan4:LatLng?,latlan5:LatLng?){
            if(latlan0!=null&&latlan1!=null&&latlan2!=null&&latlan3!=null&&latlan4!=null&&latlan5!=null) {
                var latitudes =
                    (latlan0.latitude + latlan1.latitude + latlan2.latitude + latlan3.latitude + latlan4.latitude + latlan5.latitude) / 6
                var longitudes =
                    (latlan0.longitude + latlan1.longitude + latlan2.longitude + latlan3.longitude + latlan4.longitude + latlan5.longitude) / 6
                centpo = LatLng(latitudes, longitudes)
            }
            else if(latlan0!=null&&latlan1!=null&&latlan2!=null&&latlan3!=null&&latlan4!=null&&latlan5==null){
                var latitudes =
                    (latlan0.latitude + latlan1.latitude + latlan2.latitude + latlan3.latitude + latlan4.latitude) / 5
                var longitudes =
                    (latlan0.longitude + latlan1.longitude + latlan2.longitude + latlan3.longitude + latlan4.longitude) / 5
                centpo = LatLng(latitudes, longitudes)
            }
            else if(latlan0!=null&&latlan1!=null&&latlan2!=null&&latlan3!=null&&latlan4==null&&latlan5==null){
                var latitudes =
                    (latlan0.latitude + latlan1.latitude + latlan2.latitude + latlan3.latitude) / 4
                var longitudes =
                    (latlan0.longitude + latlan1.longitude + latlan2.longitude + latlan3.longitude) / 4
                centpo = LatLng(latitudes, longitudes)
            }
            else if(latlan0!=null&&latlan1!=null&&latlan2!=null&&latlan3==null&&latlan4==null&&latlan5==null){
                var latitudes =
                    (latlan0.latitude + latlan1.latitude + latlan2.latitude) / 3
                var longitudes =
                    (latlan0.longitude + latlan1.longitude + latlan2.longitude) / 3
                centpo = LatLng(latitudes, longitudes)
            }
            else if(latlan0!=null&&latlan1!=null&&latlan2==null&&latlan3==null&&latlan4==null&&latlan5==null){
                var latitudes =
                    (latlan0.latitude + latlan1.latitude) / 2
                var longitudes =
                    (latlan0.longitude + latlan1.longitude) / 2
                centpo = LatLng(latitudes, longitudes)
            }
            else if(latlan0!=null&&latlan1==null&&latlan2==null&&latlan3==null&&latlan4==null&&latlan5==null){
                var latitudes =
                    (latlan0.latitude)
                var longitudes =
                    (latlan0.longitude)
                centpo = LatLng(latitudes, longitudes)
            }


        }





        //리버스 지오코딩 함수 - 좌표 넣으면 주소 반환
        fun revGeoc(repoint:LatLng = LatLng(37.600114, 126.864815)){

            val mGeoCoder : Geocoder = Geocoder(this)

            val mResultList : List<Address> = mGeoCoder.getFromLocation(
                repoint.latitude,
                repoint.longitude,
                1
            )

            val geocodedad : String = mResultList[0].toString()

            var str1 : Int = geocodedad.indexOf(":") +2
            var geostr1 : String = geocodedad.substring(str1 , 100)
            var str2 : Int = geostr1.indexOf("]") +24
            val geostr : String = geocodedad.substring(str1 , str2)
            revgeopo = geostr

            Toast.makeText(
                this, geostr,
                Toast.LENGTH_SHORT
            ).show()

        }


        //var texv : TextView = findViewById<TextView>(R.id.textViewadd)

        //지오코딩 함수 - 주소 넣으면 좌표 변환
        fun GeocF(addr : String = "한국항공대학교"){
            val mGeoCoder : Geocoder = Geocoder(this)

            var resultLocation: List<Address>? = null

            try {
                resultLocation = mGeoCoder.getFromLocationName(addr, 1)

            }
            catch (e:IOException){
                e.printStackTrace()
                Log.d(TAG, "주소변환 실패")
            }

            if (resultLocation != null) {
                if (resultLocation.size !=0) {
                    var resltLat: Double = resultLocation.get(0).latitude
                    var resltLng: Double = resultLocation.get(0).longitude
                    geocLatLng = LatLng(resltLat, resltLng)
                    geocstat = 1
                }
                else{
                    //texv.setText("해당되는 주소 정보는 없습니다")
                    geocstat = 0
                }

            }


        }


        ///////////////////////////
        /*fun getLatLng() : Location{
            var currentLatLng: Location? = null
            if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_LOCATION)
                getLatLng()
            } else {
                val locationProvider = LocationManager.GPS_PROVIDER
                currentLatLng = locationManager?.getLastKnownLocation(locationProvider)
            }
            return currentLatLng!!
        }


        fun getCurrentLoc() {
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager?
            var userLocation: Location = getLatLng()
            if (userLocation != null) {
                cur_latitude = userLocation.latitude
                cur_longitude = userLocation.longitude
                Log.d("CheckCurrentLocation", "현재 내 위치 값: $cur_latitude, $cur_longitude")
            }
        }



*/
////////////////////////

        fun reset(marker1:Marker?, marker2:Marker?, marker3:Marker?, marker4:Marker?){

                marker1?.map = null
                marker2?.map = null
                marker3?.map = null
                marker4?.map = null

        }



        database = FirebaseDatabase.getInstance().reference




        naverMap.setOnMapClickListener { point, coord ->

            markers[0]?.map = null

            /*Toast.makeText(
                this, "${coord.latitude}, ${coord.longitude}",
                Toast.LENGTH_SHORT
            ).show()*/
            var marker = Marker()
            marker.position = LatLng(coord.latitude, coord.longitude)
            marker.map = naverMap
            markers[0] = marker
            //count++
            /*if (intent.hasExtra("Information")) {
                val info = intent.getSerializableExtra("Information") as Information
                if (info.name != null) {
                    val user = User(info.name, info.email, coord.latitude, coord.longitude)
                    database.child("rooms").child(info.roomName).setValue(user)

                }

                //Toast.makeText(this, info.email, Toast.LENGTH_SHORT).show()

            }*/



            //리버스 지오코딩

            var mGeoCoder : Geocoder = Geocoder(this)

            var mResultList : List<Address>? = null

            mResultList = mGeoCoder.getFromLocation(
                coord.latitude,
                coord.longitude,
                10
            )

            var geocodedad : String = "a"
            geocodedad = mResultList[0].toString()

            var str1 : Int = geocodedad.indexOf(":") +2
            var geostr1 : String = geocodedad.substring(str1 , 100)
            var str2 : Int = geostr1.indexOf("]") +24
            var geostr : String = "a"
            geostr = geocodedad.substring(str1 , str2)

            Toast.makeText(
                this, geostr,
                Toast.LENGTH_SHORT
            ).show()
        }




        fun findwayfir(markeruser:Marker?):Int { //주어진 좌표와 가장 가까운 거점을 반환

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
                0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00
            )
            val temArrlon: Array<Double> = arrayOf(
                0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00
            )
            val temArrsum: Array<Double> = arrayOf(
                0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00
            )

            val numtem:Array<Int> = arrayOf(0,1,2,3,4,5,6,7,8,9,10,11,12,13)


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
                if (temArrsum[j] < temArrsum[j + 1]){
                    temArrsum[j+1] = temArrsum[j]
                    numtem[k+1] = numtem[k]
                }
                j++
                k++
            }
            var rest = numtem[13] //가장 거리가 작은 값의 위치
            return rest

        }


        fun resetOne(marker: Marker?){
            if(marker!=null){
                marker.map = null
            }
            else {
                marker?.map = null
            }
        }


        fun findwaysnd(lNguser:Marker?):String { //주어진 거점과 가장 가까운 지점 지명을 반환


            val markerarrlats: Array<Double> = arrayOf( //서울 주요지점 좌표
                37.534659,37.556830,37.556003,37.540218,37.497299,37.570125,37.505441,37.504493,37.582312,37.573341,37.613127,37.580563,37.535720,37.543971,37.533909, 37.514056, 37.529860, 37.481193, 37.479380, 37.517362, 37.581168, 37.592541, 37.578304, 37.571166
            )
            val markerarrlons: Array<Double> = arrayOf(
                126.993941,126.924531,126.936845,127.068996,127.026681,126.991239,127.004708,127.082889,126.983575,126.976816,127.030211,127.002058,126.875263,127.036595,126.901894, 126.941258, 126.964552, 126.952573, 127.011797, 127.041040, 127.048615, 127.016308, 126.900013, 127.009152
            )

            //아래부턴 두 지점의 거리 제곱 구해 저장하는 코드
            var i = 0
            val temArrlat: Array<Double> = arrayOf(
                0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00
            )
            val temArrlon: Array<Double> = arrayOf(
                0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00
            )
            val temArrsum: Array<Double> = arrayOf(
                0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00
            )
            val numtem:Array<Int> = arrayOf(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23)

            for (index in 0..23 step 1) {
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
            for (index in 0..22 step 1) {
                if (temArrsum[j] < temArrsum[j + 1]){
                    temArrsum[j+1] = temArrsum[j]
                    numtem[k+1] = numtem[k]
                }
                j++
                k++
            }
            var rest = numtem[23] //가장 거리가 작은 값의 위치

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
            else if (rest == 16) {
                endpoint = LatLng(markerarrlats[16], markerarrlons[16])
                return "용산"
            }
            else if (rest == 17) {
                endpoint = LatLng(markerarrlats[17], markerarrlons[17])
                return "서울대입구"
            }
            else if (rest == 18) {
                endpoint = LatLng(markerarrlats[18], markerarrlons[18])
                return "예술의전당"
            }
            else if (rest == 19) {
                endpoint = LatLng(markerarrlats[19], markerarrlons[19])
                return "강남구청역(청담동)"
            }
            else if (rest == 20) {
                endpoint = LatLng(markerarrlats[20], markerarrlons[20])
                return "청량리"
            }
            else if (rest == 21) {
                endpoint = LatLng(markerarrlats[21], markerarrlons[21])
                return "성신여대입구"
            }
            else if (rest == 22) {
                endpoint = LatLng(markerarrlats[22], markerarrlons[22])
                return "디지털미디어시티"
            }
            else if (rest == 23) {
                endpoint = LatLng(markerarrlats[23], markerarrlons[23])
                return "동대문"
            }
            return "없음"

        }



        var num=0
        var roomm = ""

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
                            num = 3 //3번에 들어가는 코드
                        } else if (dataSnapshot.hasChild("1") && dataSnapshot.child("1").child("email").value != info.email) {
                            num = 2 //2번에 들어가는 코드
                        } else {
                            num = 1 //1번에 들어가는 코드
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Getting Post failed, log a message
                        Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                        // ...
                    }
                })
        }

        var marker1 = Marker()
        marker1.icon = MarkerIcons.BLACK
        marker1.iconTintColor = Color.RED

        var marker2 = Marker()
        marker2.icon = MarkerIcons.BLACK
        marker2.iconTintColor = Color.BLUE

        /*database.child("rooms").child(roomm)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    if(num==1) {

                        marker1.captionText = dataSnapshot.child("2").child("username").value.toString()
                        marker1.setCaptionAligns(Align.Top)
                        val value1 = dataSnapshot.child("2").child("latitude").value
                        val value2 = dataSnapshot.child("2").child("longitude").value

                        marker1.position = LatLng(value1.toString().toDouble(), value2.toString().toDouble())
                        marker1.map = naverMap
                        markers[1] = marker1

                        marker2.captionText = dataSnapshot.child("3").child("username").value.toString()
                        marker2.setCaptionAligns(Align.Top)
                        val value3 = dataSnapshot.child("3").child("latitude").value
                        val value4 = dataSnapshot.child("3").child("longitude").value

                        marker2.position = LatLng(value3.toString().toDouble(), value4.toString().toDouble())
                        marker2.map = naverMap
                        markers[2] = marker2
                    } else if(num==2){

                        marker1.captionText = dataSnapshot.child("1").child("username").value.toString()
                        marker1.setCaptionAligns(Align.Top)
                        val value1 = dataSnapshot.child("1").child("latitude").value
                        val value2 = dataSnapshot.child("1").child("longitude").value

                        marker1.position = LatLng(value1.toString().toDouble(), value2.toString().toDouble())
                        marker1.map = naverMap
                        markers[1] = marker1

                        marker2.captionText = dataSnapshot.child("3").child("username").value.toString()
                        marker2.setCaptionAligns(Align.Top)
                        val value3 = dataSnapshot.child("3").child("latitude").value
                        val value4 = dataSnapshot.child("3").child("longitude").value

                        marker2.position = LatLng(value3.toString().toDouble(), value4.toString().toDouble())
                        marker2.map = naverMap
                        markers[2] = marker2

                    } else{
                        marker1.captionText = dataSnapshot.child("1").child("username").value.toString()
                        marker1.setCaptionAligns(Align.Top)
                        val value1 = dataSnapshot.child("1").child("latitude").value
                        val value2 = dataSnapshot.child("1").child("longitude").value

                        marker1.position = LatLng(value1.toString().toDouble(), value2.toString().toDouble())
                        marker1.map = naverMap
                        markers[1] = marker1

                        marker2.captionText = dataSnapshot.child("2").child("username").value.toString()
                        marker2.setCaptionAligns(Align.Top)
                        val value3 = dataSnapshot.child("2").child("latitude").value
                        val value4 = dataSnapshot.child("2").child("longitude").value

                        marker2.position = LatLng(value3.toString().toDouble(), value4.toString().toDouble())
                        marker2.map = naverMap
                        markers[2] = marker2

                    }


                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                    // ...
                }
            })*/





        var markerx = Marker()




        buttongps.setOnClickListener {


            Toast.makeText(
                this, "위치 추적을 시작합니다",
                Toast.LENGTH_SHORT
            ).show()

            initLocation()

            var locationRequest = LocationRequest.create()
            locationRequest.run {
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                interval = 60 * 1000
            }

            var locationCallback = object: LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    locationResult?.let {
                        resetOne(markerx)
                        for((i, location) in it.locations.withIndex()) {
                            Log.d(TAG, "#$i ${location.latitude} , ${location.longitude}")
                            latCur = location.latitude
                            lonCur = location.longitude


                            markerx.position = LatLng(latCur, lonCur)
                            markerx.icon = OverlayImage.fromResource(R.drawable.dddd)
                            markerx.width = 160
                            markerx.height =160
                            markerx.map = naverMap

                            if(inr==0){
                                val cameraUpdate = CameraUpdate.scrollTo(LatLng(latCur, lonCur))
                                    .animate(CameraAnimation.Easing, 1200)
                                naverMap.moveCamera(cameraUpdate)
                                inr++
                            }


                        }
                    }
                }
            }
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())

            /*Toast.makeText(
                this, "${latCur}",
                Toast.LENGTH_SHORT
            ).show()*/

        }




        /*var overlayImage1 = OverlayImage*/









        /*var oNe = findViewById<Button>(R.id.button1)*/

        button1.setOnClickListener { //첫번째버튼 누를시


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

            }

        }

        /*var tWo = findViewById<Button>(R.id.button3)*/

        button3.setOnClickListener { //리셋버튼
            reset(markers[0],markers[1],markers[2],markers[3])
            count=0
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

        var tHe = findViewById<Button>(R.id.button2)

        button2.setOnClickListener {
            /*val endlat = endpoint.latitude.toString()
            val endlng = endpoint.longitude.toString()
            val locations = naver.map().reverseGeocode(lat = endlat, lng = endlng)*/

            //locations.items.forEach { location -> location.address }
            //Toast.makeText(this, "${locations}",
            //    Toast.LENGTH_SHORT).show()

            if (intent.hasExtra("Information")) {
                val info = intent.getSerializableExtra("Information") as Information
                if (info.name != null && markers[count]!=null ) {
                    val arr = info.email.split("@")
                    val emailValue = arr[0]
                    val user = User(info.name, info.email, markers[count]?.position?.latitude, markers[count]?.position?.longitude)

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

            //리버스 지오코딩
            val testLG : LatLng = LatLng(37.600114, 126.864815)

            val mGeoCoder : Geocoder = Geocoder(this)

            val mResultList : List<Address> = mGeoCoder.getFromLocation(
                testLG.latitude,
                testLG.longitude,
                1
            )

            val geocodedad : String = mResultList[0].toString()

            var str1 : Int = geocodedad.indexOf(":") +2
            var geostr1 : String = geocodedad.substring(str1 , 100)
            var str2 : Int = geostr1.indexOf("]") +24
            val geostr : String = geocodedad.substring(str1 , str2)

            Toast.makeText(
                this, geostr,
                Toast.LENGTH_SHORT
            ).show()


        }






        var Chr = findViewById<Button>(R.id.buttonCh)


        buttonCh.setOnClickListener { //카메라 이동 버튼
            var CamGo = searchpo.text.toString()
            GeocF(CamGo)

            if(geocstat == 1) {
                try {
                    val cameraUpdate = CameraUpdate.scrollTo(geocLatLng)
                        .animate(CameraAnimation.Easing, 1200)
                    naverMap.moveCamera(cameraUpdate)
                } catch (e: IOException) {
                    e.printStackTrace()
                    Log.d(TAG, "올바른 주소를 입력해주세요")
                }
            }
            else if(geocstat ==0){
                //texv.setText("해당되는 주소 정보는 없습니다")
                Toast.makeText(
                    this, "해당되는 주소 정보는 없습니다",
                    Toast.LENGTH_SHORT).show()
            }

        }



    }
}

