package com.yuvarajcode.food_harbor.presentation.main.donation.organization

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.yuvarajcode.food_harbor.domain.model.Donation
import com.yuvarajcode.food_harbor.domain.model.User
import com.yuvarajcode.food_harbor.domain.usecases.DonationUseCases.DonationUseCases
import com.yuvarajcode.food_harbor.domain.usecases.userUseCases.UserUseCases
import com.yuvarajcode.food_harbor.utilities.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DonationOrgViewModel @Inject constructor(
    private val donationUseCases: DonationUseCases,
    private val auth : FirebaseAuth,
    private val userUseCases: UserUseCases
) : ViewModel() {

    private val userId = auth.currentUser?.uid

    private val _getDonationList = mutableStateOf<ResponseState<List<Donation>>>(ResponseState.Success(emptyList()))
    val getDonationList : State<ResponseState<List<Donation>>> = _getDonationList

    private val _updateStatus = mutableStateOf<ResponseState<Boolean?>>(ResponseState.Success(null))
    val updateStatus : State<ResponseState<Boolean?>> = _updateStatus

    private val _getData = mutableStateOf<ResponseState<User?>>(ResponseState.Success(null))
    val getData : State<ResponseState<User?>> = _getData

   var donateUser : User = User()


    fun getDonationList(){
        viewModelScope.launch {
            donationUseCases.getDonationList().collect {
                _getDonationList.value = it
            }
        }
    }

    fun updateStatus(id : String, status : Boolean) {
        viewModelScope.launch {
            donationUseCases.updateDonationStatus(id, status).collect {
                _updateStatus.value = it
            }
        }
    }


    fun getUserDetails(id : String?){
        viewModelScope.launch {
            if(id!= null) {
                userUseCases.getUserDetails(id).collect {
                    _getData.value = it
                }
            }
        }
    }
}