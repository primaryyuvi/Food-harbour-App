package com.yuvarajcode.food_harbor.domain.usecases.userUseCases

import com.yuvarajcode.food_harbor.domain.repository.UserRepository
import javax.inject.Inject

class GetUserDetails @Inject constructor(
    private val userRepository: UserRepository
){
    operator fun invoke(userId : String) = userRepository.getUserDetails(userId)
}