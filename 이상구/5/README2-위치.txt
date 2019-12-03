
�ڱ� ��ġ ���� �ڵ� ���� ����� README2

-------------------------------------------------------------------------------------------------------------------------



1. MapActivity �� onMapReady �Ʒ��� ���� �Լ� ����(�۹̼� ��Ȯ�ο�)



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



2. �� �Լ� ���� ���� ������ ����

	var inr = 0
	var latCur : Double? = null
        var lonCur : Double? = null
        var fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
	var markerx = Marker()




-------------------------------------------------------------------------------------------------------------------------



3. activity_map�� ��ġ ���� ���ۿ� ��ư ����. id�� buttongps


<Button
            android:id="@+id/buttongps"
            android:layout_width="50dp"
            android:layout_height="wrap_content"
            android:background="@color/colorbackground"
            android:text="����       ����" />



-------------------------------------------------------------------------------------------------------------------------



4. �ʾ�Ƽ��Ƽ �� �Ʒ� ���� �ڵ� ����, drawble ������ dddd.png ����



buttongps.setOnClickListener {


            Toast.makeText(
                this, "��ġ ������ �����մϴ�",
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


���� �ٸ� ������ ���� ��� �ڵ� ����.