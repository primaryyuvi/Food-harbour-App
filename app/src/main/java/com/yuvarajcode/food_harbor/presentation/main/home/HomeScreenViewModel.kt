package com.yuvarajcode.food_harbor.presentation.main.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.yuvarajcode.food_harbor.domain.model.Donation
import com.yuvarajcode.food_harbor.domain.model.User
import com.yuvarajcode.food_harbor.domain.repository.UserRepository
import com.yuvarajcode.food_harbor.domain.usecases.DonationUseCases.DonationUseCases
import com.yuvarajcode.food_harbor.domain.usecases.userUseCases.UserUseCases
import com.yuvarajcode.food_harbor.presentation.main.donation.user.DonationUIState
import com.yuvarajcode.food_harbor.utilities.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val donationUseCases: DonationUseCases,
    private val userUseCases: UserUseCases,
    private val userRepository: UserRepository,
    auth : FirebaseAuth,
    private val client : ChatClient
) : ViewModel() {

    private val _getDonateList = MutableStateFlow<ResponseState<List<Donation>>>(ResponseState.Success(emptyList()))
    val getDonateList : StateFlow<ResponseState<List<Donation>>> = _getDonateList

    private val _getData = MutableStateFlow<ResponseState<User?>>(ResponseState.Success(null))
    val getData : StateFlow<ResponseState<User?>> = _getData

    private val _getUserData = mutableStateOf<ResponseState<User?>>(ResponseState.Success(null))
    val getUserData : State<ResponseState<User?>> = _getUserData



    private var userId= auth.currentUser?.uid
    val donationListStates = mutableStateMapOf<String, MutableStateFlow<DonationListUIState>>()


    fun getDonationListState(donationId: String): StateFlow<DonationListUIState> {
        return donationListStates.getOrPut(donationId) {
            MutableStateFlow(DonationListUIState())
        }
    }

    fun loadDonationListState(donationId: String,userId: String) {
        viewModelScope.launch {
            launch { getUserProfilePic(donationId,userId) }
        }
    }

    fun updateDonationListState(donationId: String, update: (DonationListUIState) -> DonationListUIState) {
        donationListStates[donationId]?.update { update(it) }
    }

    fun getUserProfilePic(donationId: String,userId : String){
        viewModelScope.launch {
            userRepository.getProfilePic(userId).collect{
                updateDonationListState(donationId){donation->
                    donation.copy(profilePic = it)
                }
            }
        }
    }

    init{
        auth.addAuthStateListener {
            userId = it.currentUser?.uid
            getUserDetails()
        }
    }



    var userData = User()

    fun getUserDetails(){
        viewModelScope.launch {
            if (userId != null) {
                userUseCases.getUserDetails(userId!!).collect {
                    _getUserData.value = it
                }
            }
        }
    }
    var donateUser : User = User()

    fun getUserDetails(id : String?){
        viewModelScope.launch {
            if(id!= null) {
                userUseCases.getUserDetails(id).collect {
                    _getData.value = it
                }
            }
        }
    }


    fun getDonationList() {
        viewModelScope.launch {
            donationUseCases.getDonationList().collect {
                _getDonateList.value = it
            }
        }
    }


}