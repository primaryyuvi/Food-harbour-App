package com.yuvarajcode.food_harbor.presentation.main.donation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.yuvarajcode.food_harbor.presentation.main.donation.organization.DonationOrgViewModel
import com.yuvarajcode.food_harbor.presentation.main.donation.organization.DonationOrganisationScreen
import com.yuvarajcode.food_harbor.presentation.main.donation.user.DonationUserScreen
import com.yuvarajcode.food_harbor.presentation.main.donation.user.DonationUserScreenViewModel


@Composable
fun DonationScreen(
    navController: NavController,
    donationUserScreenViewModel: DonationUserScreenViewModel,
    donationOrgViewModel: DonationOrgViewModel,
    isUser : Boolean
) {
    Log.d("DonationScreen", "DonationScreen: $isUser")
    if(isUser){
        DonationUserScreen(
            navController = navController,
            donationScreenViewModel = donationUserScreenViewModel
        )
    }
    else{
        DonationOrganisationScreen(
            navController = navController,
            donationOrgViewModel = donationOrgViewModel
        )
    }
}
