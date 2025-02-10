package com.yuvarajcode.food_harbor.presentation.profile.donationHistory

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.yuvarajcode.food_harbor.domain.model.Donation
import com.yuvarajcode.food_harbor.domain.repository.DonationRepository
import com.yuvarajcode.food_harbor.domain.usecases.DonationUseCases.DonationUseCases
import com.yuvarajcode.food_harbor.utilities.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DonationHistoryViewModel @Inject constructor(
    private val donationUseCases: DonationUseCases,
    private val donationRepository: DonationRepository,
    auth :FirebaseAuth
) : ViewModel(){

    private var userId= auth.currentUser?.uid
    private val _getDataUser = MutableStateFlow<ResponseState<List<Donation>>>(ResponseState.Initial)
    val getDataUser : StateFlow<ResponseState<List<Donation>>> = _getDataUser

    private val _getDataOrg = MutableStateFlow<ResponseState<List<Donation>>>(ResponseState.Initial)
    val getDataOrg : StateFlow<ResponseState<List<Donation>>> = _getDataOrg

    init {
        auth.addAuthStateListener {
            userId = it.currentUser?.uid
            getDonationHistoryForUser()
            getDonationHistoryForOrg()
        }
    }

    private fun getDonationHistoryForUser(){
        viewModelScope.launch {
            if(userId!= null) {
                donationUseCases.getDonationHistory(userId!!).collect {
                    _getDataUser.value = it
                }
            }
        }
    }

    fun getDonationHistoryForOrg(){
        viewModelScope.launch {
           donationRepository.getDonationHistoryOrg(userId?: "").collect {
               _getDataOrg.value = it
           }
        }
    }
}