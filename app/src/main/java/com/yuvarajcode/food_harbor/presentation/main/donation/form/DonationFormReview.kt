package com.yuvarajcode.food_harbor.presentation.main.donation.form

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.yuvarajcode.food_harbor.R
import com.yuvarajcode.food_harbor.utilities.ResponseState
import com.yuvarajcode.food_harbor.utilities.Screens
import com.yuvarajcode.food_harbor.utilities.ToastForResponseState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonateFormReview(
    navController: NavController,
    donationFormViewModel: DonationFormViewModel
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp

    val donation by donationFormViewModel.donation.collectAsState()

    val addDonationState by donationFormViewModel.addData

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Review Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)

        ) {
            StepsIndicator(
                screenHeight = screenHeight,
                currentStep = 4
            )
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                // Food Details Section
                Section(title = "Food Details") {
                    DetailField("Food Item Name", donation.name)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        DetailField("Quantity", donation.quantity.toString(), modifier = Modifier.weight(1f))
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        DetailField("Entry Date", donation.entryDate , modifier = Modifier.weight(1f))
                        DetailField("Expiry Date", donation.expiryDate, modifier = Modifier.weight(1f))
                    }
                }

                // Description Section
                Section(title = "Description") {
                    Text(
                        text = donation.description,
                        modifier = Modifier.padding(horizontal = (screenHeight * 0.02).dp)
                    )
                }

                // Photos Section
                Section(title = "Photos") {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = (screenHeight * 0.02).dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(donation.imageUrl.size) {
                            AsyncImage(
                               model = donation.imageUrl[it],
                                contentDescription = "Rice photo ${it + 1}",
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }

                // Pickup Location Section
                Section(title = "Pickup Location") {
                    DetailField("Address", donation.location)
                    DetailField("Additional Instructions", donation.additionalDetailsOnLocation)
                }

                // Contact Information Section
                Section(title = "Contact Information") {
                    DetailField("Full Name", donation.username)
                    DetailField("Phone Number", donation.contact)
                    DetailField("Email Address", donation.email)
                }

                // Submit Button
                Button(
                    onClick = {
                        donationFormViewModel.addDonation(
                            name = donation.name,
                            quantity = donation.quantity,
                            description = donation.description,
                            imageUrl = donation.imageUrl,
                            location = donation.location,
                            contact = donation.contact,
                            entryDate = donation.entryDate,
                            expiryDate = donation.expiryDate,
                            status = donation.status,
                            time = donation.time,
                            additionalDetails = donation.additionalDetailsOnLocation,
                            username = donation.username,
                            email = donation.email
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding((screenHeight * 0.02).dp)
                        .height((screenHeight * 0.06).dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Submit Donation")
                }

                when(val response = addDonationState ){
                    is ResponseState.Error -> {
                        ToastForResponseState(response.message)
                    }
                    ResponseState.Initial -> {

                    }
                    ResponseState.Loading -> {

                    }
                    is ResponseState.Success -> {
                        ToastForResponseState("Donation added successfully")
                        navController.navigate(Screens.DonationScreen.route){
                            popUpTo(Screens.DonationFormScreen.route){
                                inclusive = true
                            }
                        }
                        donationFormViewModel.resetState()
                    }
                }
            }
        }
    }
}

@Composable
private fun Section(
    title: String,
    content: @Composable () -> Unit,
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp


        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            Text(
                text = title,
                fontSize = (screenHeight * 0.02).sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = (screenHeight * 0.02).dp, vertical = 8.dp)
            )
            Card(
                modifier = Modifier.padding((16).dp).fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(249, 250, 251)
                )
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    content()
                }
            }
        }

}

@Composable
private fun DetailField(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    Column(
        modifier = modifier.padding(horizontal = (screenHeight * 0.02).dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            fontSize = (screenHeight * 0.0175).sp,
            color = Color.Gray
        )
        Text(
            text = value,
            fontSize = (screenHeight * 0.019).sp,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}