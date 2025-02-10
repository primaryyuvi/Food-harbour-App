package com.yuvarajcode.food_harbor.presentation.main.donation.organization

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.yuvarajcode.food_harbor.domain.model.Donation
import com.yuvarajcode.food_harbor.domain.model.DonationStatus
import com.yuvarajcode.food_harbor.domain.model.OrgDonation
import com.yuvarajcode.food_harbor.domain.model.User
import com.yuvarajcode.food_harbor.domain.repository.OrganisationRepository
import com.yuvarajcode.food_harbor.domain.usecases.DonationUseCases.DonationUseCases
import com.yuvarajcode.food_harbor.domain.usecases.userUseCases.UserUseCases
import com.yuvarajcode.food_harbor.utilities.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class DonationOrgViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val orgRepo: OrganisationRepository,
    private val donationUseCases: DonationUseCases,
    private val userUseCases: UserUseCases
) : ViewModel() {

    private var userId = auth.currentUser?.uid
    var user: User = User()


    private val _getDonationList =
        MutableStateFlow<ResponseState<List<Donation>>>(ResponseState.Initial)
    val getDonationList: StateFlow<ResponseState<List<Donation>>> = _getDonationList

    private val _donateeList =
        MutableStateFlow<ResponseState<List<OrgDonation>>>(ResponseState.Initial)
    val donateeList: StateFlow<ResponseState<List<OrgDonation>>> = _donateeList

    init {
        auth.addAuthStateListener {
            userId = it.currentUser?.uid
            userId?.let { it1 ->
                getDonationListByOrgId(it1)
                getDonationList(it1)
                getUserDetails(it1)
            }
        }
    }

    private fun getDonationListByOrgId(orgId: String) {
        viewModelScope.launch {
            orgRepo.getDonationsByOrgId(orgId).collect { response ->
                _donateeList.value = response
            }
        }
    }

    fun getUserDetails(userId: String) {
        viewModelScope.launch {
            userUseCases.getUserDetails(userId).collect {
                if (it is ResponseState.Success) {
                    user = it.data
                }
            }
        }
    }

    fun getDonationList(userId: String) {
        viewModelScope.launch {
            orgRepo.getDonationList(userId).collect {
                Log.d("DonationOrgViewModel", "getDonationList: $it")
                _getDonationList.value = it
            }
        }
    }

    private val _addDonatee = mutableStateOf<ResponseState<Boolean>>(ResponseState.Success(false))
    val addDonatee: State<ResponseState<Boolean>> = _addDonatee

    private val _updateAccept = mutableStateOf<ResponseState<Boolean?>>(ResponseState.Initial)
    val updateAccept: State<ResponseState<Boolean?>> = _updateAccept

    private val _updateReject = mutableStateOf<ResponseState<Boolean?>>(ResponseState.Initial)
    val updateReject: State<ResponseState<Boolean?>> = _updateReject


    private val orgDonationStates = mutableMapOf<String, MutableStateFlow<OrgDonationUIState>>()
    private val donationOrgStates = mutableMapOf<String, MutableStateFlow<DonationOrgUIState>>()


    fun getOrgDonationState(donationId: String): StateFlow<OrgDonationUIState> {
        return orgDonationStates.getOrPut(donationId) {
            MutableStateFlow(OrgDonationUIState())
        }
    }

    fun getDonationOrgState(donationId: String): StateFlow<DonationOrgUIState> {
        return donationOrgStates.getOrPut(donationId) {
            MutableStateFlow(DonationOrgUIState())
        }
    }


    fun loadOrgDonationData(donationId: String) {
        viewModelScope.launch {
            launch { getDonationDetails(donationId) }
        }
    }


    fun updateDonationStatus(donationId: String, newStatus: String) {
        viewModelScope.launch {
            try {
                orgRepo.updateDonationStatus(donationId, newStatus, userId!!).collect {
                    Log.d("DonationOrgViewModel", "updateDonationStatus: $it")
                }
            } catch (e: Exception) {
                Log.d("DonationOrgViewModel", "updateDonationStatus: ${e.message}")
            }
        }
    }


    fun getDonationDetails(donationId: String) {
        viewModelScope.launch {
            orgRepo.getDonationDetails(donationId).collect {
                updateOrgDonationStates(donationId) { orgDonation ->
                    orgDonation.copy(donation = it)
                }
            }
        }
    }


    fun updateOrgDonationStates(
        donationId: String,
        update: (OrgDonationUIState) -> OrgDonationUIState
    ) {
        orgDonationStates[donationId]?.update(update)
    }

    fun updateDonationOrgStates(
        donationId: String,
        update: (DonationOrgUIState) -> DonationOrgUIState
    ) {
        donationOrgStates[donationId]?.update(update)
    }


    fun addDonateeToDonation(donationId: String, userId: String = this.userId!!) {
        viewModelScope.launch {
            orgRepo.addDonateeToDonation(donationId, userId).collect {
                updateDonationOrgStates(donationId) { donationOrg ->
                    donationOrg.copy(acceptDonation = it)
                }
            }
        }
    }

    fun rejectDonateeStatus(donationId: String) {
        viewModelScope.launch {
            userId?.let {
                orgRepo.updateRejectDonation(donationId, it).collect {
                    updateOrgDonationStates(donationId) { donationOrg ->
                        donationOrg.copy(removeDonation = it)
                    }
                }
            }
        }
    }

    fun receivedDonation(donationId: String) {
        viewModelScope.launch {
            orgRepo.updateDonationStatus(
                donationId,
                DonationStatus.DONATIONRECEIVED.status,
                userId!!
            ).collect {
                updateOrgDonationStates(donationId) { donationOrg ->
                    donationOrg.copy(receivedStatus = it)
                }
            }
        }
    }

    fun transactionCompleteDonation(
        donationId: String,
    ) {
        viewModelScope.launch {
            orgRepo.transactionCompleteStatus(donationId, userId!!, user.userName).collect {
                updateOrgDonationStates(donationId) { donationOrg ->
                    donationOrg.copy(transactionStatus = it)
                }
            }
        }
    }

    fun resetDonationOrgStatus(donationId: String) {
        updateDonationOrgStates(donationId) { DonationOrgUIState() }
    }


}