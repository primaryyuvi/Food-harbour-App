package com.yuvarajcode.food_harbor.domain.usecases.DonationUseCases

data class DonationUseCases(
    val getDonationDetails: GetDonationDetails,
    val getDonationList: GetDonationList,
    val getDonationListByUserId: GetDonationListByUserId,
    val deleteDonation: DeleteDonation,
    val addDonation: AddDonation,
    val updateDonationStatus: UpdateDonationStatus
)
