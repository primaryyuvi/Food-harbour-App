package com.yuvarajcode.food_harbor.domain.usecases.authenticationUseCases

import com.yuvarajcode.food_harbor.domain.repository.AuthenticationRepository
import javax.inject.Inject

class FireBaseSignIn @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    operator fun invoke(email: String, password: String) =
        authenticationRepository.firebaseSignIn(email, password)
}