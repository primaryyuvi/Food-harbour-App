package com.yuvarajcode.food_harbor.presentation.profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.yuvarajcode.food_harbor.domain.model.Donation
import com.yuvarajcode.food_harbor.domain.model.User
import com.yuvarajcode.food_harbor.domain.repository.DonationRepository
import com.yuvarajcode.food_harbor.domain.usecases.DonationUseCases.DonationUseCases
import com.yuvarajcode.food_harbor.domain.usecases.userUseCases.UserUseCases
import com.yuvarajcode.food_harbor.utilities.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewmodel @Inject constructor(
    private val userUseCases : UserUseCases,
    private val donationUseCases: DonationUseCases,
    private val donationRepository: DonationRepository,
    auth : FirebaseAuth,
) : ViewModel(){
    private var userId= auth.currentUser?.uid
    private val _getData = mutableStateOf<ResponseState<User?>>(ResponseState.Success(null))
    val getData : State<ResponseState<User?>> = _getData

    private val _setData = mutableStateOf<ResponseState<Boolean?>>(ResponseState.Initial)
    val setData : State<ResponseState<Boolean?>> = _setData

    var realObj : User = User()

    private val _chatLogout = mutableStateOf<ResponseState<Boolean?>>(ResponseState.Success(null))
    val chatLogout : State<ResponseState<Boolean?>> = _chatLogout

    private val _getHistory = MutableStateFlow<ResponseState<List<Donation>>>(ResponseState.Success(emptyList()))
    val getHistory : StateFlow<ResponseState<List<Donation>>> = _getHistory

    private val _getHistoryOrg = MutableStateFlow<ResponseState<List<Donation>>>(ResponseState.Success(emptyList()))
    val getHistoryOrg : StateFlow<ResponseState<List<Donation>>> = _getHistoryOrg

    init {
        auth.addAuthStateListener {
            userId = it.currentUser?.uid
            getUserDetails()
            getDonationHistory()
            getDonationHistoryOrg()
        }
    }
      fun getUserDetails(){
        viewModelScope.launch {
            if(userId!= null) {
                userUseCases.getUserDetails(userId!!).collect {
                    _getData.value = it
                }
            }
        }
    }

    fun setUserState(){
        _setData.value = ResponseState.Success(null)
    }
    fun setUserDetails(
        username : String,
        email : String,
        password:String,
        profilePicture: String,
        phone : String,
        missionStatement : String,
        weeklyGoal : Int,
        monthlyGoal : Int,
        yearlyGoal : Int,
        name : String
    ){
        viewModelScope.launch {
            if (userId != null) {
                userUseCases.setUserDetails(
                    userId = userId!!,
                    username = username,
                    email = email,
                    password = password,
                    profilePicture = profilePicture,
                    name = name,
                    missionStatement = missionStatement,
                    weeklyGoal = weeklyGoal,
                    monthlyGoal = monthlyGoal,
                    yearlyGoal = yearlyGoal,
                    phone = phone
                ).collect {
                    _setData.value = it
                }
            }
        }
    }

    private fun getDonationHistory(){
        viewModelScope.launch {
            if(userId!= null) {
                donationUseCases.getDonationHistory(userId!!).collect {
                    _getHistory.value = it
                }
            }
        }
    }

    private fun getDonationHistoryOrg(){
        viewModelScope.launch {
            if(userId!= null) {
                donationRepository.getDonationHistoryOrg(userId!!).collect {
                    _getHistoryOrg.value = it
                }
            }
        }
    }

    fun resetSetData(){
        _setData.value = ResponseState.Initial
    }


}