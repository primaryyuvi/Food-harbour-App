package com.yuvarajcode.food_harbor.domain.usecases.DonationUseCases

import com.yuvarajcode.food_harbor.domain.repository.DonationRepository
import javax.inject.Inject

class GetDonationListByUserId @Inject constructor(
    private val donationRepository: DonationRepository
){
    suspend operator fun invoke(userId : String) = donationRepository.getDonationListByUserId(userId)
}