package com.yuvarajcode.food_harbor.domain.usecases

import com.yuvarajcode.food_harbor.domain.repository.AuthenticationRepository
import javax.inject.Inject

class GetAuthState @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
)   {
    operator fun invoke() = authenticationRepository.getAuthState()
}