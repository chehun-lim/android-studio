package com.example.firebase

import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.UiThread
import androidx.fragment.app.FragmentActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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


class MainActivity : AppCompatActivity(),  OnMapReadyCallback {

    val TAG = "MyMessage"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }

        mapFragment.getMapAsync(this)
























        // Write a message to the database
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

                /*val accounts = arrayOf(value)
                val list : ListView = findViewById<ListView>(R.id.account_list)

                var adapter =  ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1,accounts)

                list.adapter = adapter*/
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

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


        var a = findViewById<Button>(R.id.button1)

        button1.setOnClickListener {
            destination(markers[0],markers[1],markers[2],markers[3])
        }

        var b = findViewById<Button>(R.id.button3)

        button3.setOnClickListener {
            reset(markers[0],markers[1],markers[2],markers[3])
            count=0
        }



    }

}
