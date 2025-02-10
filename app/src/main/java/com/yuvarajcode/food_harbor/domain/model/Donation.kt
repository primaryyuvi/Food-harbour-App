package com.yuvarajcode.food_harbor.domain.model

import com.google.gson.annotations.SerializedName

data class Donation(
    var id: String = "",
    var userId : String = "",
    var name: String ="",
    var description: String = "",
    var quantity : Double = 0.0,
    var imageUrl: List<String> = emptyList(),
    var location: String = "",
    var additionalDetailsOnLocation : String = "",
    var contact: String = "",
    var entryDate: String = "",
    var expiryDate : String = "",
    var time: String = "",
    @SerializedName("status")
    var status: String = "",
    var donateeList : List<String> = emptyList(),
    var acceptedBy : String = "",
    var email : String = "",
    var username : String = "",
    var donateeName : String = "",
)

enum class DonationStatus (val status : String,val value : String?) {
    PENDING("Pending",null),
    ACCEPTEDBYORG("Accepted by Organization","NGO"),
    ACCEPTEDBYDONOR("Accepted by Donor","Donor"),
    REJECTEDBYDONOR("Rejected by Donor","Donor"),
    DONATIONRECEIVED("Donation Received","Received"),
    DONATIONGIVEN("Donation Given","Given"),
    EXPIRED("Expired",null),
    TRANSACTIONCOMPLETE("Transaction Complete","Complete"),
}