package com.yuvarajcode.food_harbor.domain.repository

import com.yuvarajcode.food_harbor.domain.model.User
import com.yuvarajcode.food_harbor.utilities.ResponseState
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserDetails(userId : String) : Flow<ResponseState<User>>
    fun setUserDetails(userId: String,username : String,email : String,password : String,profilePicture : String,phoneNumber : String) : Flow<ResponseState<Boolean>>
}