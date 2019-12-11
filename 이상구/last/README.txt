1. button1.setOnClickListener 아래 마커 생성부 아래에 다음 추가

infoWindow.adapter = object : InfoWindow.DefaultTextAdapter(this) {
                override fun getText(infoWindow: InfoWindow): CharSequence {
                    return savepoint.toString()
                }
            }



            marker.tag = "마커 1"
            marker.setOnClickListener {
                infoWindow.open(marker)
                true

            }
            marker.setOnClickListener {
                if (rest == 0) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragview, ResultFrag())
                        .commit()
                }
                else if (rest == 1) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragview, ResultFrag2())
                        .commit()
                }
                else if (rest == 2) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragview, ResultFrag3())
                        .commit()
                }
                else if (rest == 3) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragview, ResultFrag4())
                        .commit()
                }
                else if (rest == 4) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragview, ResultFrag5())
                        .commit()
                }
                else if (rest == 5) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragview, ResultFrag6())
                        .commit()
                }
                else if (rest == 6) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragview, ResultFrag7())
                        .commit()
                }
                else if (rest == 7) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragview, ResultFrag8())
                        .commit()
                }
                else if (rest == 8) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragview, ResultFrag9())
                        .commit()
                }
                else if (rest == 9) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragview, ResultFrag10())
                        .commit()
                }
                else if (rest == 10) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragview, ResultFrag11())
                        .commit()
                }
                else if (rest == 11) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragview, ResultFrag12())
                        .commit()
                }
                else if (rest == 12) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragview, ResultFrag13())
                        .commit()
                }
                else if (rest == 13) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragview, ResultFrag14())
                        .commit()
                }
                else if (rest == 14) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragview, ResultFrag15())
                        .commit()
                }
                else if (rest == 15) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragview, ResultFrag16())
                        .commit()
                }
                else if (rest == 16) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragview, ResultFrag17())
                        .commit()
                }
                else if (rest == 17) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragview, ResultFrag18())
                        .commit()
                }
                else if (rest == 18) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragview, ResultFrag19())
                        .commit()
                }
                else if (rest == 19) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragview, ResultFrag20())
                        .commit()
                }
                else if (rest == 20) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragview, ResultFrag21())
                        .commit()
                }
                else if (rest == 21) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragview, ResultFrag22())
                        .commit()
                }
                else if (rest == 22) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragview, ResultFrag23())
                        .commit()
                }
                else if (rest == 23) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragview, ResultFrag23())
                        .commit()
                }

                true

            }



2. naverMap.setOnMapClickListener 각각 위, 아래에 추가

val infoWindow = InfoWindow()

////////////////////

infoWindow.close()


3. fragment_result 싹다 붙여넣기

4. strings의 app_name 바꾸기

5. drawble 싹다 붙여넣기

6. menifest에서

android:icon="@mipmap/ic_launcher_title"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_title"

이렇게 수정하기

7. 맵액티비티 변수 선언부에 var rest : Int = 0 추가,
원래 rest가 정의된 findwaysnd함수 부분의 rest 구문을 rest = numtem[23] 이렇게 바꿈


8. (카카오톡 사진이 안되어있을시) 카카오링크 액티비티의

ContentObject.newBuilder(
                        "어디서 만나지?",

아래부분의 링크를

https://postfiles.pstatic.net/MjAxOTExMjRfMjgy/MDAxNTc0NTgzOTE1MDYy.UKpceVxCgiNVXpLc11ALfszlkuKTWviD9itXOrt7p9sg.ILVLigju-GyRRvIDckb4G1HLinNhlZV3H8prKkwkVzQg.PNG.lsk9955/imtem.png?type=w580

로 바꿈
