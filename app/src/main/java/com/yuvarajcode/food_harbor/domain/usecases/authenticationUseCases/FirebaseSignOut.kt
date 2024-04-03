package com.yuvarajcode.food_harbor.domain.usecases.authenticationUseCases

import com.yuvarajcode.food_harbor.domain.repository.AuthenticationRepository
import javax.inject.Inject

class FirebaseSignOut @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    operator fun invoke() = authenticationRepository.firebaseSignOut()
}
