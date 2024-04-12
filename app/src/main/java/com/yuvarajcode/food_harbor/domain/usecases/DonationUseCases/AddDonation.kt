package com.yuvarajcode.food_harbor.domain.usecases.DonationUseCases

import com.yuvarajcode.food_harbor.domain.model.DonationStatus
import com.yuvarajcode.food_harbor.domain.repository.DonationRepository
import javax.inject.Inject

class AddDonation @Inject constructor(
    private val donationRepository: DonationRepository
) {
    suspend operator fun invoke(
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
        userId: String
    ) = donationRepository.addDonation(
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
    )
}