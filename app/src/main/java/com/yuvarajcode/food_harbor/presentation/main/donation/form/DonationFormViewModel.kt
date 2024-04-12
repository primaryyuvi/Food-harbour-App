package com.yuvarajcode.food_harbor.presentation.main.donation.form

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.yuvarajcode.food_harbor.domain.usecases.DonationUseCases.DonationUseCases
import com.yuvarajcode.food_harbor.utilities.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DonationFormViewModel @Inject constructor(
    private val donationUseCases: DonationUseCases,
    private val auth : FirebaseAuth
) : ViewModel(){

    private val userId = auth.currentUser?.uid
    private val _addData = mutableStateOf<ResponseState<Boolean?>>(ResponseState.Success(null))
    val addData : State<ResponseState<Boolean?>> = _addData

    fun addDonation(
        name: String,
        description: String,
        quantity: Double,
        imageUrl: List<String>,
        location: String,
        contact: String,
        entryDate: String,
        expiryDate: String,
        time: String,
        status: Boolean?
    ) {
        viewModelScope.launch {
            if (userId != null) {
                donationUseCases.addDonation(
                    name,
                    description,
                    quantity,
                    imageUrl,
                    location,
                    contact,
                    entryDate,
                    expiryDate,
                    time,
                    status,
                    userId
                ).collect {
                    _addData.value = it
                }
            }
        }
    }

}