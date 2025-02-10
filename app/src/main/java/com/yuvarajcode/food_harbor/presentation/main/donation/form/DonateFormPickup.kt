package com.yuvarajcode.food_harbor.presentation.main.donation.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yuvarajcode.food_harbor.utilities.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonateFormPickup(
    donationFormViewModel: DonationFormViewModel,
    navController: NavController
) {
    var address by remember { mutableStateOf("") }
    var instructions by remember { mutableStateOf("") }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("Donate Food") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ){
        Column(
            modifier = Modifier.padding(it).padding((screenHeight * 0.02).dp)
        ) {
            StepsIndicator(
                screenHeight = screenHeight,
                currentStep = 2
            )
            Text(
                text = "Pickup Location",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = (screenHeight * 0.02).dp, bottom = 8.dp),
                fontWeight = FontWeight.Bold,
            )

            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Address") },
                placeholder = { Text("Enter pickup address") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Default.LocationOn, "Location")
                }
            )

            // Map placeholder

            OutlinedTextField(
                value = instructions,
                onValueChange = { instructions = it },
                label = { Text("Additional Instructions") },
                placeholder = { Text("E.g. Building access, parking information, etc.") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            Button(
                onClick = {
                    navController.navigate(Screens.DonationFormContactDetails.route)
                    donationFormViewModel.addPickupDetailsToDonation(
                        address,
                        instructions
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = (screenHeight * 0.02).dp)
                    .height((screenHeight * 0.06).dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Next Step")
            }
        }
    }
}