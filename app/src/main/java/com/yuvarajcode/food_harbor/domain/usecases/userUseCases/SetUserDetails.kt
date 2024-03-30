package com.yuvarajcode.food_harbor.domain.usecases.userUseCases

import com.yuvarajcode.food_harbor.domain.repository.UserRepository
import javax.inject.Inject

class SetUserDetails @Inject constructor(
    private val userRepository: UserRepository
){
    operator fun invoke(
        userId : String,
        username : String,
        email : String,
        password:String,
        profilePicture:String,
        phone : String) = userRepository.setUserDetails(userId, username, email, password, profilePicture ,phone)
}