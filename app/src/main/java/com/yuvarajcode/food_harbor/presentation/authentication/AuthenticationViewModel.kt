package com.yuvarajcode.food_harbor.presentation.authentication

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuvarajcode.food_harbor.domain.usecases.AuthenticationUseCases
import com.yuvarajcode.food_harbor.utilities.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authUseCases : AuthenticationUseCases
) : ViewModel() {
    val isUserAuthenticated get() = authUseCases.isUserAuthenticated()

    private val _signIn = mutableStateOf<ResponseState<Boolean>>(ResponseState.Success(false))
    val signIn : State<ResponseState<Boolean>> = _signIn

    private val _signOut = mutableStateOf<ResponseState<Boolean>>(ResponseState.Success(false))
    val signOut: State<ResponseState<Boolean>> = _signOut

    private val _register = mutableStateOf<ResponseState<Boolean>>(ResponseState.Success(false))
    val register : State<ResponseState<Boolean>> = _register

    private val isUser = mutableStateOf(false)
    val isUserState : State<Boolean> = isUser
    fun isUserOrNot(
        value : Boolean
    ){
        isUser.value = value
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            authUseCases.fireBaseSignIn(email, password).collect{
                _signIn.value = it
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authUseCases.fireBaseSignOut().collect{
                _signOut.value = it
                if(it==ResponseState.Success(true)){
                    _signIn.value = ResponseState.Success(false)
                }
            }
        }
    }

    fun register(name: String, userName: String, phoneNumber: String, email: String, password: String) {
        viewModelScope.launch {
            authUseCases.firebaseRegister(name, userName, phoneNumber, email, password,isUserState.value).collect{
                _register.value = it
            }
        }
    }
}