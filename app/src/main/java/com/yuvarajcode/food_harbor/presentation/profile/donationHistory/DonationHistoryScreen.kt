package com.yuvarajcode.food_harbor.presentation.profile.donationHistory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yuvarajcode.food_harbor.domain.model.Donation
import com.yuvarajcode.food_harbor.presentation.profile.main.DonationItem
import com.yuvarajcode.food_harbor.utilities.ResponseState

@Composable
fun DonationHistoryScreen(
    navController: NavController,
    isUser : Boolean,
    donationHistoryViewModel: DonationHistoryViewModel
) {
    val historyUserState by donationHistoryViewModel.getDataUser.collectAsState()
    val historyOrgState by donationHistoryViewModel.getDataOrg.collectAsState()

     val historyUser = when(historyUserState) {
         is ResponseState.Success -> (historyUserState as ResponseState.Success<List<Donation>>).data
         else -> emptyList()
     }

    val historyOrg = when(historyOrgState) {
        is ResponseState.Success -> (historyOrgState as ResponseState.Success<List<Donation>>).data
        else -> emptyList()
    }

    Scaffold (
        topBar = {
            HistoryTopBar(
                navController = navController
            )
        },
    ){
        if(isUser) {
            if (historyUser.isEmpty()) {
                Column(
                    modifier = Modifier.padding(it).fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No Donation History",
                        fontSize = 16.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.padding(it)
                ) {
                    items(historyUser.size) { index ->
                        val donation = historyUser[index]
                        DonationItem(
                            title = donation.name,
                            organization = donation.donateeName,
                            status = donation.status,
                            date = donation.time
                        )
                    }
                }
            }
        }else{
            if (historyOrg.isEmpty()) {
                Column(
                    modifier = Modifier.padding(it).fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No Donation History",
                        fontSize = 16.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.padding(it).padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(historyOrg.size) { index ->
                        val donation = historyOrg[index]
                        DonationItem(
                            title = donation.name,
                            organization = donation.donateeName,
                            status = donation.status,
                            date = donation.time
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DonationHistoryItem(donation: Donation) {
    var showDetails by remember{
        mutableStateOf(false)
    }
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color(
                red = 7,
                green = 31,
                blue = 27,
                alpha = 255
            ),
            contentColor = Color.White
        )
    ) {
        Column() {
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ){
                    Text(
                        text = "Donation to ${donation.acceptedBy}",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(8.dp),
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis
                    )
                TextButton(
                    onClick = {
                        showDetails = !showDetails
                    },
                    modifier = Modifier
                        .padding(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (showDetails) "Hide Details" else "Show Details",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            if(showDetails) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = donation.name,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(8.dp),
                    )
                }
                Row {
                    Text(
                        text = "Description : ${donation.description}",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Quantity : ${donation.quantity} Kg",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryTopBar(
    navController: NavController
) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = "Donation History")
        },
        navigationIcon = {
             IconButton(
                 onClick = {
                     navController.navigateUp()
                 }
             ) {
                 Icon(
                     imageVector = Icons.Filled.ArrowBack,
                     contentDescription = "Back",
                 )
             }
        }
    )
}
