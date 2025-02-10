package com.yuvarajcode.food_harbor.domain.repository

import com.yuvarajcode.food_harbor.domain.model.Donation
import com.yuvarajcode.food_harbor.domain.model.OrgDonation
import com.yuvarajcode.food_harbor.domain.model.User
import com.yuvarajcode.food_harbor.utilities.ResponseState
import kotlinx.coroutines.flow.Flow

interface OrganisationRepository {
    suspend fun getDonationsByOrgId(orgId: String): Flow<ResponseState<List<OrgDonation>>>
    suspend fun getDonationList(userId: String) : Flow<ResponseState<List<Donation>>>
    suspend fun getDonationDetails (donationId: String): Flow<ResponseState<Donation>>
    suspend fun updateAcceptDonation(donationId: String, userId: String): Flow<ResponseState<Boolean>>
    suspend fun updateDonationStatus(donationId: String, status: String,userId :String): Flow<ResponseState<Boolean>>
    suspend fun updateRejectDonation(donationId: String, userId: String): Flow<ResponseState<Boolean>>
    suspend fun getOrgStatus(userId :String,orgId: String): Flow<ResponseState<String>>
    suspend fun addDonateeToDonation(donationId: String, userId: String): Flow<ResponseState<Boolean>>
    suspend fun transactionCompleteStatus(donationId: String, userId: String,username : String): Flow<ResponseState<Boolean>>
}