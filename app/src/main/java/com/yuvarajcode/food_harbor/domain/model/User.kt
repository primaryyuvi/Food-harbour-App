package com.yuvarajcode.food_harbor.domain.model

import com.google.firebase.firestore.PropertyName

data class User(
    var userId: String = "" ,
    var name: String = "",
    var userName: String="",
    var profilePictureUrl: String= "",
    var password : String="",
    var email : String="",
    @get: PropertyName ("isUser")
    @set: PropertyName ("isUser")
    var isUser : Boolean = false,
    var phoneNumber : String="",
)
