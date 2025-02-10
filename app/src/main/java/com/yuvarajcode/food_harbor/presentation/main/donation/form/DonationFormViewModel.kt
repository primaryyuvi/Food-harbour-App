package com.yuvarajcode.food_harbor.presentation.main.donation.form

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.yuvarajcode.food_harbor.domain.model.Donation
import com.yuvarajcode.food_harbor.domain.model.OrgDonation
import com.yuvarajcode.food_harbor.domain.repository.OrganisationRepository
import com.yuvarajcode.food_harbor.domain.usecases.DonationUseCases.DonationUseCases
import com.yuvarajcode.food_harbor.utilities.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DonationFormViewModel @Inject constructor(
    private val donationUseCases: DonationUseCases,
    private val auth : FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val orgRepo : OrganisationRepository
) : ViewModel(){

    private var userId = auth.currentUser?.uid
    private val _addData = mutableStateOf<ResponseState<Boolean?>>(ResponseState.Initial)
    val addData : State<ResponseState<Boolean?>> = _addData

    private val _donation = MutableStateFlow<Donation>(Donation())
    val donation : StateFlow<Donation> = _donation



    init {
        auth.addAuthStateListener {
            userId = it.currentUser?.uid
        }
    }

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
        status: String,
        additionalDetails: String,
        email: String,
        username: String
    ) {
        viewModelScope.launch {
            val id = firestore.collection("donations").document().id
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
                    userId!!,
                    id,
                    additionalDetails,
                    email,
                    username
                ).collect {
                    _addData.value = it
                }
            }
        }
    }


    fun addFoodDetailsToDonation(
        foodName: String,
        quantity: String,
        description: String,
        pickupDate: String,
        expiryDate: String,
        photos: List<Uri?>
    ){
        viewModelScope.launch {
            _donation.value = _donation.value.copy(
                name = foodName,
                quantity = quantity.toDouble(),
                description = description,
                entryDate = pickupDate,
                expiryDate = expiryDate,
                imageUrl = photos.map { it.toString() }
            )
        }
    }

    fun addContactDetailsToDonation(
        fullName : String,
        contact: String,
        email : String
    ) {
        viewModelScope.launch {
            _donation.value = _donation.value.copy(
                contact = contact,
                userId = userId!!,
                username = fullName,
                email = email
            )
        }
    }

    fun addPickupDetailsToDonation(
        location : String,
        additionalDetails : String
    ) {
        viewModelScope.launch {
            _donation.value = _donation.value.copy(
                location = location,
                additionalDetailsOnLocation = additionalDetails
            )
        }
    }



    fun resetState() {
        _addData.value = ResponseState.Initial
    }

}