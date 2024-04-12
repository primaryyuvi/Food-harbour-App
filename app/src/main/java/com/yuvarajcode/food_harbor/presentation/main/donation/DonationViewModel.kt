package com.yuvarajcode.food_harbor.presentation.main.donation

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
class DonationViewModel @Inject constructor(
    private val donationUseCases: DonationUseCases,
    private val auth : FirebaseAuth
) : ViewModel()
{

    private val userId = auth.currentUser?.uid
    private val _donationList = mutableStateOf<ResponseState<List<Donation>>>(ResponseState.Success(emptyList()))
    val donationList : State<ResponseState<List<Donation>>> = _donationList



    private val _getDonation = mutableStateOf<ResponseState<Donation?>>(ResponseState.Success(null))
    val getDonation : State<ResponseState<Donation?>> = _getDonation


    init {
        getDonationList()
    }




    private fun getDonationList(){
        viewModelScope.launch {
            donationUseCases.getDonationList().collect {
                _donationList.value = it
            }
        }
    }

    fun getDonationDetails(donationId : String){
        viewModelScope.launch {
            if (userId != null) {
                donationUseCases.getDonationDetails(userId,donationId).collect {
                    _getDonation.value = it
                }
            }
        }
    }

}