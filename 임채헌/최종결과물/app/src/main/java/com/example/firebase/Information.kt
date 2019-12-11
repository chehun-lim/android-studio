package com.example.firebase

import java.io.Serializable

class Information : Serializable{ //유저 정보를 다음 activity로 넘기기 위해 만듬
    var userId: String = ""
    var name: String = ""
    var email: String = ""
    var roomName: String = "no room"
}

data class User( //유저 정보를 firebase서버에 올리기 위해 만듬
    var username: String= "",
    var email: String = "",
    var latitude: Double? = 0.00,
    var longitude: Double? = 0.00


)

data class miniUser( var username: String= "", //좌표 값을 제외한 유저 정보를 firebase서버에 올리기 위해 만듬
                     var email: String = "")
