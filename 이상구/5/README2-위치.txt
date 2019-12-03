
자기 위치 추적 코드 적용 설명용 README2

-------------------------------------------------------------------------------------------------------------------------



1. MapActivity 의 onMapReady 아래에 다음 함수 정의(퍼미션 재확인용)



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



-------------------------------------------------------------------------------------------------------------------------



2. 위 함수 위에 다음 변수들 정의

	var inr = 0
	var latCur : Double? = null
        var lonCur : Double? = null
        var fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
	var markerx = Marker()




-------------------------------------------------------------------------------------------------------------------------



3. activity_map에 위치 추적 시작용 버튼 생성. id는 buttongps


<Button
            android:id="@+id/buttongps"
            android:layout_width="50dp"
            android:layout_height="wrap_content"
            android:background="@color/colorbackground"
            android:text="추적       시작" />



-------------------------------------------------------------------------------------------------------------------------



4. 맵액티비티 맨 아래 다음 코드 복붙, drawble 폴더에 dddd.png 복붙



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



-------------------------------------------------------------------------------------------------------------------------


이후 다른 내용이 있을 경우 코드 참고.