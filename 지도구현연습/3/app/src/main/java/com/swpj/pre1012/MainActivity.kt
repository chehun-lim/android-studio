package com.swpj.pre1012

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.fragment.app.FragmentActivity
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : FragmentActivity(), OnMapReadyCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }

        mapFragment.getMapAsync(this)
    }


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


        val markers = arrayOfNulls<Marker>(4)
        var count = 0
        var sndpoint : Int = 0
        var endpoint : LatLng = LatLng(0.000000,0.000000)
        var markerend : Marker = Marker()


        val markerdest = Marker()

        markerdest.position = LatLng(37.5670135, 126.9783740)
        markers[3] = markerdest

        fun destination(marker1:Marker?, marker2:Marker?, marker3:Marker?, marker4:Marker?){
            if(marker1!=null&&marker2!=null&&marker3!=null&&marker4!=null) {
                var latitudes =
                    (marker1.position.latitude + marker2.position.latitude + marker3.position.latitude) / 3
                var longitudes =
                    (marker1.position.longitude + marker2.position.longitude + marker3.position.longitude) / 3
                marker4.position = LatLng(latitudes, longitudes)
                marker4.map = naverMap
            }
        }

        fun reset(marker1:Marker?, marker2:Marker?, marker3:Marker?, marker4:Marker?){
            if(marker1!=null&&marker2!=null&&marker3!=null&&marker4!=null) {
                marker1.map = null
                marker2.map = null
                marker3.map = null
                marker4.map = null
            }
            else{
                marker1?.map = null
                marker2?.map = null
                marker3?.map = null
                marker4?.map = null
            }
        }
        fun resetOne(marker: Marker?){
            if(marker!=null){
                marker.map = null
            }
            else {
                marker?.map = null
            }
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




        fun findwaysnd(lNguser:Marker?):String { //주어진 거점과 가장 가까운 지점 지명을 반환


            val markerarrlats: Array<Double> = arrayOf( //서울 주요지점 좌표
                37.534659,37.556830,37.556003,37.540218,37.497299,37.570125,37.505441,37.504493,37.582312,37.573341,37.613127,37.580563,37.535720,37.543971
                )
            val markerarrlons: Array<Double> = arrayOf(
                126.993941,126.924531,126.936845,127.068996,127.026681,126.991239,127.004708,127.082889,126.983575,126.976816,127.030211,127.002058,126.875263,127.036595
            )

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
                if (temArrsum[j] < temArrsum[j + 1]){
                    temArrsum[j+1] = temArrsum[j]
                    numtem[k+1] = numtem[k]
                }
                j++
                k++
            }
            var rest = numtem[13] //가장 거리가 작은 값의 위치

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
                return "대학로"
            }
            else if (rest == 12) {
                endpoint = LatLng(markerarrlats[12], markerarrlons[12])
                return "목동"
            }
            else if (rest == 13) {
                endpoint = LatLng(markerarrlats[13], markerarrlons[13])
                return "서울숲"
            }
            return "없음"

        }





        naverMap.setOnMapClickListener { point, coord ->
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
        }


        var oNe = findViewById<Button>(R.id.button1)

        button1.setOnClickListener { //첫번째버튼 누를시
            destination(markers[0],markers[1],markers[2],markers[3]) //1차 최단거리장소
            var savepoint = findwaysnd(markers[3]) //최종 추천장소 이름
            Toast.makeText(this, "${savepoint}",
                Toast.LENGTH_SHORT).show()
            var marker = Marker()
            marker.position = endpoint //endpoint가 최종 추천장소
            marker.map = naverMap
            markerend = marker

        }

        var tWo = findViewById<Button>(R.id.button3)

        button3.setOnClickListener { //리셋버튼
            reset(markers[0],markers[1],markers[2],markers[3])
            count=0
            resetOne(markerend)
        }



    }
    }

