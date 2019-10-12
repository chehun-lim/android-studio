package com.swpj.pre1012

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.fragment.app.FragmentActivity
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker


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



        var cnt_1 = 0
        var marker_1 = Marker()
        var marker_2 = Marker()
        var marker_3 = Marker()

        naverMap.setOnMapClickListener { point, coord ->
                if(cnt_1<3) {
                    Toast.makeText(
                        this, "${coord.latitude}, ${coord.longitude}",
                Toast.LENGTH_SHORT
                ).show()
                val marker = Marker()
                    marker.position = LatLng(coord.latitude, coord.longitude)
                    marker.map = naverMap

                    if(cnt_1==0){
                        marker_1.position = LatLng(coord.latitude, coord.longitude)
                    }
                    else if(cnt_1==1){
                        marker_2.position = LatLng(coord.latitude, coord.longitude)
                    }
                    else{
                        marker_3.position = LatLng(coord.latitude, coord.longitude)
                    }

                    cnt_1++
                }
            }

        if(cnt_1==3) {
            var lat_1 = (marker_1.position.latitude + marker_2.position.latitude + marker_3.position.latitude)/3
            var lon_1 = (marker_1.position.longitude + marker_2.position.longitude + marker_3.position.longitude)/3

            naverMap.setOnMapClickListener { point, coord ->
                val marker = Marker()
                marker.position = LatLng(lat_1, lon_1)
                marker.map = naverMap

            }
        }
        }
    }

