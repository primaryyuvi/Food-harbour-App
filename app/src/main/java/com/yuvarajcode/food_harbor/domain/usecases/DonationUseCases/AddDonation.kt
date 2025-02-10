package com.yuvarajcode.food_harbor.domain.usecases.DonationUseCases

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
        status: String,
        userId: String,
        id : String,
        additionalDetails: String,
        email: String,
        username: String
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
        userId,
        id,
        additionalDetails,
        email,
        username
    )
}