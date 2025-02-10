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
    var weeklyGoal : Int = 0,
    var monthlyGoal : Int = 0,
    var yearlyGoal : Int = 0,
    var totalDonation : Int = 0,
    var ngosDonated : Int = 0,
    var noOfDonations : Int = 0,
    var missionStatement : String = "",
)
