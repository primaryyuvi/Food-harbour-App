package com.yuvarajcode.food_harbor.domain.repository

import com.google.firebase.auth.AuthResult
import com.yuvarajcode.food_harbor.utilities.ResponseState
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    fun isUserAuthenticated(): Boolean
    fun getAuthState() : Flow<Boolean>
    fun firebaseSignIn(email: String,password: String) : Flow<ResponseState<Boolean>>
    fun firebaseSignOut() : Flow<ResponseState<Boolean>>
    fun firebaseRegister(name : String, username : String, phoneNumber : String, email: String, password: String, isUser : Boolean) : Flow<ResponseState<Boolean>>
}