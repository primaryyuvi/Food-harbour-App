package com.yuvarajcode.food_harbor.presentation.main.donation.user

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.yuvarajcode.food_harbor.domain.model.Donation
import com.yuvarajcode.food_harbor.domain.usecases.DonationUseCases.DonationUseCases
import com.yuvarajcode.food_harbor.utilities.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DonationUserScreenViewModel @Inject constructor(
    private val donationUseCases: DonationUseCases,
    private val auth : FirebaseAuth
) : ViewModel() {
    private val userId = auth.currentUser?.uid
    private val _donationByUserId = mutableStateOf<ResponseState<List<Donation>>>(ResponseState.Success(emptyList()))
    val donationByUserId : State<ResponseState<List<Donation>>> = _donationByUserId


    fun getDonationListByUserId(){
        viewModelScope.launch {
            if (userId != null) {
                donationUseCases.getDonationListByUserId(userId).collect {
                    _donationByUserId.value = it
                }
            }
        }
    }

    private val _deleteData = mutableStateOf<ResponseState<Boolean?>>(ResponseState.Success(null))
    val deleteData : State<ResponseState<Boolean?>> = _deleteData

    fun deleteDonation(id : String){
        viewModelScope.launch {
            donationUseCases.deleteDonation(id).collect {
                _deleteData.value = it
            }
        }
    }
}