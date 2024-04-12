package com.yuvarajcode.food_harbor.presentation.main.donation.user

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.yuvarajcode.food_harbor.domain.model.Donation
import com.yuvarajcode.food_harbor.presentation.main.BottomNavigation
import com.yuvarajcode.food_harbor.presentation.main.BottomNavigationScreens
import com.yuvarajcode.food_harbor.utilities.ResponseState
import com.yuvarajcode.food_harbor.utilities.Screens
import com.yuvarajcode.food_harbor.utilities.ToastForResponseState

@Composable
fun DonationUserScreen(
    navController: NavController,
    donationScreenViewModel: DonationUserScreenViewModel
) {
    donationScreenViewModel.getDonationListByUserId()
    when (val response = donationScreenViewModel.donationByUserId.value) {
        is ResponseState.Error ->
        {
            ToastForResponseState(message =response.message)
        }
        is ResponseState.Loading ->
            CircularProgressIndicator()
        is ResponseState.Success ->
        {
            val donationList = response.data
            Log.d("DonationScreen", "DonationScreen: $donationList")
            DonationShowScreen(
                navController = navController,
                donationList = donationList,
                donationScreenViewModel = donationScreenViewModel
            )
        }
    }
}
@Composable
fun DonationShowScreen(
    navController: NavController,
    donationList: List<Donation>,
    donationScreenViewModel: DonationUserScreenViewModel
) {
    Scaffold (
        bottomBar = {
            BottomNavigation(
                selectedButton = BottomNavigationScreens.Donation, navController = navController
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screens.DonationFormScreen.route)
                },
                shape = CircleShape,
                containerColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
                contentColor = Color.White,
                modifier = Modifier.size(60.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add ,
                    contentDescription = "Adding a donation"
                )
            }
        },
        topBar = {
            DonationTopBar()
        }
    ){
        Box (
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            if (donationList.isEmpty()) {
                Text(
                    text = "No Donations made yet!!",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(donationList) { donation ->
                        DonationCard(
                            donation = donation,
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonationTopBar() {
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
//    navController: NavController,
    donation: Donation,
    // donationScreenViewModel: DonationScreenViewModel
){
    var showDetails by remember { mutableStateOf(false) }
    val status = donation.status
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
            contentColor = Color.White
        )
    ) {
        Column(){
            Row (
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    text = "Name : ${donation.name}",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(8.dp),
                    fontWeight = FontWeight.Bold
                )
            }
            Row {
                Text(
                    text = "Description : ${donation.description}",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(8.dp)
                )
            }
            Row {
                Text(
                    text = "Quantity : ${donation.quantity} Kg",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(8.dp)
                )
            }
            if(showDetails)
            {
                Row(
                ) {
                    Text(
                        text = "Location : ${donation.location}",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Contact : ${donation.contact}",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(8.dp)
                    )
                    Text(
                        text = "Time : ${donation.time}",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Entry Date : ${donation.entryDate}",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(8.dp)
                    )
                    Text(
                        text = "Expiry Date : ${donation.expiryDate}",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Column {
                    Text(
                        text = if(donation.imageUrl.isEmpty()) "No Images available!!" else "Images :",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(8.dp)
                    )
                    LazyRow {
                        items(donation.imageUrl) { imageUrl ->
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = "Donation Image",
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(70.dp),
                            )
                        }
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {},
                    modifier = Modifier
                        .padding(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = when(status){
                            null -> Color.Yellow
                            true -> Color.Green
                            false -> Color.Red
                        },
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    when(status){
                        null -> Text(text = "Status : Pending")
                        true -> Text(text = "Status : Accepted")
                        false -> Text(text = "Status : Expired")
                    }

                }
                Button(
                    onClick = {
                        showDetails = !showDetails
                    },
                    modifier = Modifier
                        .padding(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if(showDetails) "Hide Details" else "Show Details"
                    )
                }
            }
            if(status == true){
                Button(
                    onClick = {
                        //donationScreenViewModel.deleteDonation(donation.id)
                    },
                    modifier = Modifier
                        .padding(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    )
                ) {
                    Text(text = "Safe Delete")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDonationScreen() {
    DonationCard(
        donation = Donation(
            name = "Food",
            description = "Food",
            quantity = 10.0,
            imageUrl = listOf(""),
            location = "Chennai",
            contact = "1234567890",
            entryDate = "2022-10-10",
            expiryDate = "2022-10-10",
            time = "10:00",
            status = null,
        )
    )
}
