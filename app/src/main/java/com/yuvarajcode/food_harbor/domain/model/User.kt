package com.yuvarajcode.food_harbor.domain.model

data class User(
    val userId: String="" ,
    val name: String="",
    val userName: String="",
    val profilePictureUrl: String?= null,
    val password : String="",
    val email : String="",
    val phoneNumber : String="",
    val isUser : Boolean = false
)
