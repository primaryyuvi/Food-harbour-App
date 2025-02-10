package com.yuvarajcode.food_harbor.domain.usecases.DonationUseCases

import com.yuvarajcode.food_harbor.domain.repository.DonationRepository
import javax.inject.Inject

class UpdateDonationStatus @Inject constructor(
    private val donationRepository: DonationRepository
){
   // suspend operator fun invoke(id: String, status: String?) = donationRepository.updateDonationStatus(id, status)
}