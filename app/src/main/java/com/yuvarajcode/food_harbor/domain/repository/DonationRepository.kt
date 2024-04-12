package com.yuvarajcode.food_harbor.domain.repository

import com.yuvarajcode.food_harbor.domain.model.Donation
import com.yuvarajcode.food_harbor.domain.model.DonationStatus
import com.yuvarajcode.food_harbor.utilities.ResponseState
import kotlinx.coroutines.flow.Flow

interface DonationRepository {
    suspend fun addDonation(
        name: String,
        description: String,
        quantity: Double,
        imageUrl: List<String>,
        location: String,
        contact: String,
        entryDate: String,
        expiryDate: String,
        time: String,
        status: Boolean?,
        userId : String
    ): Flow<ResponseState<Boolean>>
    suspend fun getDonationList() : Flow<ResponseState<List<Donation>>>
    suspend fun getDonationListByUserId(userId : String) : Flow<ResponseState<List<Donation>>>
    suspend fun getDonationDetails(userid : String,donationId : String) : Flow<ResponseState<Donation>>
    suspend fun deleteDonation(id : String) : Flow<ResponseState<Boolean>>
    suspend fun updateDonationStatus(id : String, status : Boolean?) : Flow<ResponseState<Boolean>>
}