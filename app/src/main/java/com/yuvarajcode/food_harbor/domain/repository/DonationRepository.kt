package com.yuvarajcode.food_harbor.domain.repository

import com.yuvarajcode.food_harbor.domain.model.Donation
import com.yuvarajcode.food_harbor.domain.model.User
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
        status: String,
        userId : String,
        id : String,
        additionalDetails: String,
        email: String,
        username: String
    ): Flow<ResponseState<Boolean>>
    suspend fun getDonationList() : Flow<ResponseState<List<Donation>>>
    suspend fun getDonationListByUserId(userId : String) : Flow<ResponseState<List<Donation>>>
    suspend fun deleteDonation(id : String) : Flow<ResponseState<Boolean>>
    suspend fun donateToOrg(id : String, status: String, orgId : String) : Flow<ResponseState<Boolean>>
    suspend fun getDonationHistory(userId : String) : Flow<ResponseState<List<Donation>>>
    suspend fun getDonationStatus(id : String) : Flow<ResponseState<String>>
    suspend fun acceptDonateeStatus(donationId : String, status : String, name : String) : Flow<ResponseState<Boolean>>
    suspend fun rejectDonateeStatus(donationId : String, status : String, orgId : String) : Flow<ResponseState<Boolean>>
    suspend fun getUserById(userId : String) : Flow<ResponseState<User>>
    suspend fun getUsernamesByUserIds(userIds : List<String>) : Flow<ResponseState<List<Pair<String,String>>>>
    suspend fun getDonationHistoryOrg(orgId : String) : Flow<ResponseState<List<Donation>>>
}