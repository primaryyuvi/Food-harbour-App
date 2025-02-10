package com.yuvarajcode.food_harbor.presentation.main.donation.user

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.yuvarajcode.food_harbor.domain.model.Donation
import com.yuvarajcode.food_harbor.domain.model.DonationStatus
import com.yuvarajcode.food_harbor.domain.repository.OrganisationRepository
import com.yuvarajcode.food_harbor.domain.usecases.DonationUseCases.DonationUseCases
import com.yuvarajcode.food_harbor.domain.usecases.userUseCases.UserUseCases
import com.yuvarajcode.food_harbor.utilities.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.yuvarajcode.food_harbor.domain.model.User
import com.yuvarajcode.food_harbor.domain.repository.DonationRepository
import io.getstream.chat.android.client.ChatClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

@HiltViewModel
class DonationUserScreenViewModel @Inject constructor(
    private val donationUseCases: DonationUseCases,
    private val donationRepository: DonationRepository,
    private val auth: FirebaseAuth,
    private val organisationRepository: OrganisationRepository,
    private val client : ChatClient,
) : ViewModel() {
    private var userId = auth.currentUser?.uid

    init {
        auth.addAuthStateListener {
            userId = it.currentUser?.uid
            userId?.let { it1 -> getDonationList(it1) }
        }
    }

    private val _getDonations =
        MutableStateFlow<ResponseState<List<Donation>>>(ResponseState.Success(emptyList()))
    val getDonations: StateFlow<ResponseState<List<Donation>>> = _getDonations

    private val _deleteDonation =
        mutableStateOf<ResponseState<Boolean>>(ResponseState.Success(false))
    val deleteDonation: State<ResponseState<Boolean>> = _deleteDonation

    private val _updateStatus =
        MutableStateFlow<ResponseState<Boolean>>(ResponseState.Success(false))
    val updateStatus: StateFlow<ResponseState<Boolean>> = _updateStatus

    private val _updateDonateeStatus =
        MutableStateFlow<ResponseState<Boolean>>(ResponseState.Success(false))
    val updateDonateeStatus: StateFlow<ResponseState<Boolean>> = _updateDonateeStatus



    val donationStates = mutableStateMapOf<String, MutableStateFlow<DonationUIState>>()
    val donateeStates = mutableStateMapOf<String, MutableStateFlow<DonateeUIState>>()


    private val _channelCreate = mutableStateOf<ResponseState<Boolean?>>(ResponseState.Initial)
    val channelCreate : State<ResponseState<Boolean?>> = _channelCreate



    fun createChannel(donatee : String,donationId: String){
        viewModelScope.launch {
            val channelId = UUID.randomUUID().toString()
            client.createChannel(
                channelType = "commerce",
                channelId = channelId,
                memberIds = listOf(userId.toString(), donatee),
                extraData = emptyMap()
            ).enqueue {
                if(it.isSuccess){
                    updateDonateeState(donationId,donatee){donatee->
                        donatee.copy(channelState = ResponseState.Success(true))
                    }
                    Log.d("ChannelCreate", "createChannel: ${it.data()}")
                }
                else
                {
                    updateDonateeState(donationId,donatee){donatee->
                        donatee.copy(channelState = ResponseState.Error(it.error().message ?: "An unexpected error occurred"))
                    }
                    Log.d("ChannelCreate", "createChannel: ${it.error().message}")
                }
            }
        }
    }

    fun getDonationState(donationId: String): StateFlow<DonationUIState> {
        return donationStates.getOrPut(donationId) {
            MutableStateFlow(DonationUIState())
        }
    }

    fun getDonateeState(
        donateeId: String,
        donationId: String
    ): StateFlow<DonateeUIState> {
        val id = donateeId +  donationId
        return donateeStates.getOrPut(id) {
            MutableStateFlow(DonateeUIState())
        }
    }

    fun updateDonationState(
        donationId: String,
        update: (DonationUIState) -> DonationUIState
    ) {
        donationStates[donationId]?.update { update(it) }
    }

    fun updateDonateeState(
        donationId: String,
        donateeId : String,
        update: (DonateeUIState) -> DonateeUIState
    ) {
        val id = donateeId +  donationId
        donateeStates[id]?.update { update(it) }
    }


    fun loadDonationData(donationId: String, userId: String) {
        viewModelScope.launch {
            launch { getStatus(donationId) }
        }
    }

    fun loadDonateeData(donationId: String, donateeId: String) {
        viewModelScope.launch {
            launch { getUserById(donateeId, donationId) }
        }
    }


    fun deleteDonationState(donationId: String) {
        donationStates.remove(donationId)
    }


    fun getDonationList(userId: String) {
        viewModelScope.launch {
            donationUseCases.getDonationListByUserId(userId).collect {
                _getDonations.value = it
            }
        }
    }

    fun acceptDonateeStatus(userId: String, status: String, donationId: String) {
        viewModelScope.launch {
            donationRepository.acceptDonateeStatus(donationId, status, userId).collect {
                if (it is ResponseState.Success) {
                    updateDonateeState(donationId, userId) { donatee ->
                        donatee.copy(acceptStatus = it)
                    }
                }
            }
        }
    }

    fun rejectDonateeStatus(userId: String, status: String, donationId: String) {
        viewModelScope.launch {
            donationRepository.rejectDonateeStatus(donationId, status, userId).collect {
                if (it is ResponseState.Success) {
                    updateDonateeState(donationId, userId) { donatee ->
                        donatee.copy(rejectStatus = it)
                    }
                }
            }
        }
    }

    fun deleteDonation(id: String) {
        viewModelScope.launch {
            donationUseCases.deleteDonation(id).collect {
                _deleteDonation.value = it
            }
        }
    }

    fun updateDonationStatus(id: String, status: String,orgId : String) {
        viewModelScope.launch {
            donationRepository.donateToOrg(id, status,orgId).collect {
                if (it is ResponseState.Success) {
                    updateDonationState(id) { donation ->
                        donation.copy(donateStatus = it)
                    }
                }
            }
        }
    }

    fun getStatus(id: String) {
        viewModelScope.launch {
            Log.d("DonationUserScreenViewModel", "getStatus: $id")
            donationRepository.getDonationStatus(id).collect {
                Log.d("DonationUserScreenViewModel", "getStatus: ${it}")
                    updateDonationState(id) { donation ->
                        Log.d("DonationUserScreenViewModel", "getStatusForUpdate: ${it}")
                        donation.copy(status = it)
                    }
            }
        }
    }

    fun getUserById(
        userId: String,
        donationId: String
    ) {
        viewModelScope.launch {
            donationRepository.getUserById(userId).collect {
                if (it is ResponseState.Success) {
                   updateDonateeState(
                       donationId,
                       userId
                     ) { donatee ->
                        donatee.copy(user = it)
                   }
                }
            }
        }
    }

    fun getUsernamesByUserIds(userIds: List<String>,donationId: String) {
        viewModelScope.launch {
            donationRepository.getUsernamesByUserIds(userIds).collect {
                Log.d("DonationUserScreenViewModel", "getUsernamesByUserIds: $it")
                updateDonationState(donationId){donation->
                    donation.copy(getUsername = it)
                }
            }
        }
    }

    fun transactionCompleteDonation(donationId: String,orgId: String){
        viewModelScope.launch {
            organisationRepository.updateDonationStatus(donationId, DonationStatus.TRANSACTIONCOMPLETE.status, orgId).collect {
                updateDonationState(donationId) { donation ->
                    donation.copy(transactionStatus = it)
                }
            }
        }
    }

    fun resetChannelCreate(){
        _channelCreate.value = ResponseState.Initial
    }

    override fun onCleared() {
        super.onCleared()
        donationStates.clear()
        donateeStates.clear()
    }


}