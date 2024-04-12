package com.yuvarajcode.food_harbor.presentation.main.donation.organization

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.yuvarajcode.food_harbor.domain.model.Donation
import com.yuvarajcode.food_harbor.presentation.main.BottomNavigation
import com.yuvarajcode.food_harbor.presentation.main.BottomNavigationScreens
import com.yuvarajcode.food_harbor.utilities.ResponseState
import com.yuvarajcode.food_harbor.utilities.ToastForResponseState

@Composable
fun DonationOrganisationScreen(
    navController: NavController,
    donationOrgViewModel: DonationOrgViewModel
) {
    donationOrgViewModel.getDonationList()
    when(val response = donationOrgViewModel.getDonationList.value) {
        is ResponseState.Error ->
            ToastForResponseState(message =response.message)
        is ResponseState.Loading ->
           CircularProgressIndicator(
                modifier = Modifier.fillMaxSize()
           )
        is ResponseState.Success ->
        {
           val donationList = response.data
            DonationOrgShowScreen(
                navController = navController,
                donationList = donationList,
                donationOrgViewModel = donationOrgViewModel
            )
        }
    }
}

@Composable
fun DonationOrgShowScreen(
    navController: NavController,
    donationList: List<Donation>,
    donationOrgViewModel: DonationOrgViewModel
) {
    val onRejectDonation: (Int) -> Unit = { index ->
        donationList.toMutableList().removeAt(index)
    }
    Scaffold (
        topBar = {
            DonationOrgTopBar()
        },
        bottomBar = {
            BottomNavigation(
                selectedButton = BottomNavigationScreens.Donation, navController = navController
            )
        }
    ){
        Box (
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ){
            if(donationList.isEmpty()){
                Text(
                    text = "No Donations received yet!!",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            else
            {
                LazyColumn {
                    items(donationList.size) { index ->
                        DonationCard(
                            donation = donationList[index],
                            donationOrgViewModel = donationOrgViewModel,
                            onRejectDonation = { onRejectDonation(index) }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonationOrgTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Donation",
                fontWeight = FontWeight.Bold,
            )
        },
        colors = TopAppBarColors(
            containerColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
            scrolledContainerColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White,
            titleContentColor = Color.White
        )
    )
}

@Composable
fun DonationCard(
    donation: Donation,
    donationOrgViewModel: DonationOrgViewModel,
    onRejectDonation: () -> Unit
) {
    donationOrgViewModel.getUserDetails(donation.userId)
    when(val response = donationOrgViewModel.getData.value) {
        is ResponseState.Error ->
            ToastForResponseState(message =response.message)
        is ResponseState.Loading ->
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize()
            )
        is ResponseState.Success ->
        {
            val user = response.data
            if (user != null) {
                donationOrgViewModel.donateUser = user
            }
        }
    }
    val userData = donationOrgViewModel.donateUser
    var showDetails by remember {
        mutableStateOf(false)
    }
    Card(

    ) {
        Column(

        ){
            Row {
                Text(
                    text = "Name : ${userData.name}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Row {
                Text(
                    text = "Donation Name : ${donation.name}",
                    style = MaterialTheme.typography.labelLarge,
                )
            }
            Row {
                Text(
                    text = "Donation Quantity : ${donation.quantity}",
                    style = MaterialTheme.typography.labelLarge,
                )
            }
            Row {
                Text(
                    text = "Donation Quantity : ${donation.quantity}",
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            if(showDetails)
            {
                Row {
                    Text(
                        text = "Donation Address : ${donation.location}",
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
                Row {
                    Text(
                        text = "Donation Phone : ${donation.contact}",
                        style = MaterialTheme.typography.labelLarge,
                    )
                    Text(text ="Donation Time : ${donation.time}",
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
                Row{
                    Text(
                        text = if(donation.imageUrl.isEmpty()) "No Images available!!" else "Images :",
                        style = MaterialTheme.typography.labelLarge,
                    )
                    donation.imageUrl.forEach {
                        AsyncImage(
                            model = it,
                            contentDescription = "Donation Image",
                            modifier = Modifier
                                .padding(5.dp)
                                .size(48.dp),
                        )
                    }
                }
                Row {
                    Text(
                        text = "Donation Entry Date : ${donation.entryDate}",
                        style = MaterialTheme.typography.labelLarge,
                    )
                    Text(text = "Donation Expiry Date : ${donation.expiryDate}",
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Button(
                    onClick = { 
                        donationOrgViewModel.updateStatus(donation.id, true)
                    }
                ) {
                    Text(text = "Accept")
                }
                Button(
                    onClick = {
                        onRejectDonation()
                    }
                ) {
                    Text(text = "Reject")
                }
                Button(
                    onClick = {
                    showDetails = !showDetails
                }
                ) {
                    Text(
                        text = if (showDetails) "Hide Details" else "Show Details"
                    )
                }
            }
        }
    }
}

