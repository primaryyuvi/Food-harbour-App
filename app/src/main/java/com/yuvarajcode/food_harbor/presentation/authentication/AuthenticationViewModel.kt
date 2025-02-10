package com.yuvarajcode.food_harbor.presentation.authentication

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.yuvarajcode.food_harbor.domain.repository.StreamApi
import com.yuvarajcode.food_harbor.domain.usecases.authenticationUseCases.AuthenticationUseCases
import com.yuvarajcode.food_harbor.domain.usecases.userUseCases.UserUseCases
import com.yuvarajcode.food_harbor.presentation.main.chat.StreamTokenProvider
import com.yuvarajcode.food_harbor.presentation.profile.ProfileViewmodel
import com.yuvarajcode.food_harbor.utilities.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authUseCases : AuthenticationUseCases,
    private val auth : FirebaseAuth,
    private val client : ChatClient,
    private val userUseCases: UserUseCases
) : ViewModel() {
    val isUserAuthenticated get() = authUseCases.isUserAuthenticated()

    var userId = auth.currentUser?.uid
    var email = auth.currentUser?.email

    private val _signIn = mutableStateOf<ResponseState<Boolean?>>(ResponseState.Initial)
    val signIn : State<ResponseState<Boolean?>> = _signIn

    private val _signOut = mutableStateOf<ResponseState<Boolean?>>(ResponseState.Initial)
    val signOut: State<ResponseState<Boolean?>> = _signOut

    private val _register = mutableStateOf<ResponseState<Boolean?>>(ResponseState.Initial)
    val register : State<ResponseState<Boolean?>> = _register

    private val _chatLogin = mutableStateOf<ResponseState<Boolean?>>(ResponseState.Initial)
    val chatLogin : State<ResponseState<Boolean?>> = _chatLogin

    private val _getUser = mutableStateOf<ResponseState<com.yuvarajcode.food_harbor.domain.model.User>>(ResponseState.Success(com.yuvarajcode.food_harbor.domain.model.User()))
    val getUser : State<ResponseState<com.yuvarajcode.food_harbor.domain.model.User>> = _getUser

    var userDetails : com.yuvarajcode.food_harbor.domain.model.User = com.yuvarajcode.food_harbor.domain.model.User()

    fun getUserDetailsForAuth(){
        viewModelScope.launch {
            userId?.let {
                userUseCases.getUserDetails(it).collect{
                    _getUser.value = it
                    if(it is ResponseState.Success){
                        userDetails = it.data
                    }
                }
            }
        }
    }

    var isUserState : Boolean = false
    fun isUserOrNot(
        value : Boolean
    ){
        isUserState = value
    }

    init
    {
        auth.addAuthStateListener {
            userId = it.currentUser?.uid
            getUserDetailsForAuth()
        }
    }


    private val api = StreamApi()
    private val tokenProvider = StreamTokenProvider(api)
    fun streamLogin(){
        auth.addAuthStateListener {
            userId = it.currentUser?.uid
            getUserDetailsForAuth()
        }
        userId?.let {
            client.connectUser(
                User(
                    id = it,
                    extraData = mutableMapOf(
                        "name" to userDetails.name,
                    ),
                    role = "user"
                ),
                tokenProvider.getToken(it)
            ).enqueue {
                if (it.isSuccess) {
                    _chatLogin.value = ResponseState.Success(true)
                    Log.d("ChatLogin", "streamLogin: ${it.data()}")
                } else {
                    _chatLogin.value = ResponseState.Error(it.error().message ?: "An unexpected error occurred")
                    Log.e("ChatLogin", "streamLogin: ${it.error().message}")
                }
            }
        }
    }

    fun signIn(email: String, password: String)  {
        viewModelScope.launch {
            authUseCases.fireBaseSignIn(email, password).collect{
                _signIn.value = it
                _signOut.value = ResponseState.Initial
                auth.addAuthStateListener {
                    userId = it.currentUser?.uid
                }
                Log.d("SignIn", "signIn: $userId")
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authUseCases.fireBaseSignOut().collect{
                _signOut.value = it
                if(it==ResponseState.Success(true)){
                    _signIn.value = ResponseState.Success(null)
                    _chatLogin.value = ResponseState.Success(null)
                    auth.addAuthStateListener {
                        userId = it.currentUser?.uid
                    }
                    }
                }
            }
        }

    fun register(name: String, userName: String, phoneNumber: String, email: String, password: String) {
        viewModelScope.launch {
            authUseCases.firebaseRegister(name, userName, phoneNumber, email, password,isUserState).collect{
                _register.value = it
            }
            _signOut.value = ResponseState.Initial
        }
    }

    fun chatLogout(){
        viewModelScope.launch {
            client.disconnect()
        }
    }

}