1. button1.setOnClickListener �Ʒ� ��Ŀ ������ �Ʒ��� ���� �߰�

infoWindow.adapter = object : InfoWindow.DefaultTextAdapter(this) {
                override fun getText(infoWindow: InfoWindow): CharSequence {
                    return savepoint.toString()
                }
            }



            marker.tag = "��Ŀ 1"
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



2. naverMap.setOnMapClickListener ���� ��, �Ʒ��� �߰�

val infoWindow = InfoWindow()

////////////////////

infoWindow.close()


3. fragment_result �ϴ� �ٿ��ֱ�

4. strings�� app_name �ٲٱ�

5. drawble �ϴ� �ٿ��ֱ�

6. menifest����

android:icon="@mipmap/ic_launcher_title"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_title"

�̷��� �����ϱ�

7. �ʾ�Ƽ��Ƽ ���� ����ο� var rest : Int = 0 �߰�,
���� rest�� ���ǵ� findwaysnd�Լ� �κ��� rest ������ rest = numtem[23] �̷��� �ٲ�


8. (īī���� ������ �ȵǾ�������) īī����ũ ��Ƽ��Ƽ��

ContentObject.newBuilder(
                        "��� ������?",

�Ʒ��κ��� ��ũ��

https://postfiles.pstatic.net/MjAxOTExMjRfMjgy/MDAxNTc0NTgzOTE1MDYy.UKpceVxCgiNVXpLc11ALfszlkuKTWviD9itXOrt7p9sg.ILVLigju-GyRRvIDckb4G1HLinNhlZV3H8prKkwkVzQg.PNG.lsk9955/imtem.png?type=w580

�� �ٲ�
