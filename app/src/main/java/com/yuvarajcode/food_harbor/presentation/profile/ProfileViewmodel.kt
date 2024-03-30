package com.yuvarajcode.food_harbor.presentation.profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.yuvarajcode.food_harbor.domain.model.User
import com.yuvarajcode.food_harbor.domain.usecases.userUseCases.UserUseCases
import com.yuvarajcode.food_harbor.utilities.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewmodel @Inject constructor(
    private val userUseCases : UserUseCases,
    auth :FirebaseAuth
) : ViewModel(){
    val userId= auth.currentUser?.uid
    private val _getData = mutableStateOf<ResponseState<User?>>(ResponseState.Success(null))
    val getData : State<ResponseState<User?>> = _getData

    private val _setData = mutableStateOf<ResponseState<Boolean>>(ResponseState.Success(false))
    val setData : State<ResponseState<Boolean>> = _setData

    fun getUserDetails(){
        viewModelScope.launch {
            if(userId!= null) {
                userUseCases.getUserDetails(userId).collect {
                    _getData.value = it
                }
            }
        }
    }

    fun setUserDetails(
        username : String,
        email : String,
        password:String,
        profilePicture:String,
        phone : String
    ){
        viewModelScope.launch {
            if (userId != null) {
                userUseCases.setUserDetails(
                    userId,
                    username,
                    email,
                    password,
                    profilePicture,
                    phone
                ).collect {
                    _setData.value = it
                }
            }
        }
    }
}