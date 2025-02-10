package com.yuvarajcode.food_harbor.domain.model

data class OrgDonation(
    var id : String = "",
    var accepted : Boolean = false,
    var rejected : Boolean = false,
    var status: String = "",
)

