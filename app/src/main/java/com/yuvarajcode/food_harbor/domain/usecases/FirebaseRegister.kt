package com.yuvarajcode.food_harbor.domain.usecases

import com.yuvarajcode.food_harbor.domain.repository.AuthenticationRepository
import javax.inject.Inject

class FirebaseRegister @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
){
    operator fun invoke(name : String,userName : String,phoneNumber : String,email: String, password: String,isUser :Boolean) =
        authenticationRepository.firebaseRegister(name,userName,phoneNumber,email, password,isUser)
}