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
        name : String,
        missionStatement : String,
        weeklyGoal : Int,
        monthlyGoal : Int,
        yearlyGoal : Int,
        phone : String) = userRepository.setUserDetails(
        userId = userId,
        username = username,
        email = email,
        password = password,
        profilePicture = profilePicture,
        phoneNumber = phone,
        weeklyGoal = weeklyGoal,
        monthlyGoal = monthlyGoal,
        yearlyGoal = yearlyGoal,
        missionStatement = missionStatement,
        name = name
    )
}