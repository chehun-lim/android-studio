package com.example.firebase

import java.io.Serializable

class Information : Serializable{
    var userId: String = ""
    var name: String = ""
    var email: String = ""
    var roomName: String = "no room"
}

data class User(
    var username: String= "",
    var email: String = "",
    var latitude: Double? = 0.00,
    var longitude: Double? = 0.00


)

data class miniUser( var username: String= "",
                     var email: String = "")
