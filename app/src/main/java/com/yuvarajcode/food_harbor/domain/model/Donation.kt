package com.yuvarajcode.food_harbor.domain.model

data class Donation(
    val id: String = "",
    val userId : String = "",
    val name: String ="",
    val description: String = "",
    val quantity : Double = 0.0,
    val imageUrl: List<String> = emptyList(),
    val location: String = "",
    val contact: String = "",
    val entryDate: String = "",
    val expiryDate : String = "",
    val time: String = "",
    val status: Boolean? = null
)

enum class DonationStatus {
    PENDING,
    ACCEPTED,
    EXPIRED
}