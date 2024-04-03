package com.yuvarajcode.food_harbor.domain.usecases.authenticationUseCases

data class AuthenticationUseCases(
    val isUserAuthenticated: IsUserAuthenticated,
    val getAuthState: GetAuthState,
    val fireBaseSignIn: FireBaseSignIn,
    val fireBaseSignOut: FirebaseSignOut,
    val firebaseRegister: FirebaseRegister
)
