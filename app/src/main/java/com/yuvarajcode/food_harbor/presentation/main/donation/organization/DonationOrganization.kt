package com.yuvarajcode.food_harbor.presentation.main.donation.organization

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.yuvarajcode.food_harbor.domain.model.Donation
import com.yuvarajcode.food_harbor.domain.model.DonationStatus
import com.yuvarajcode.food_harbor.domain.model.OrgDonation
import com.yuvarajcode.food_harbor.presentation.main.BottomNavigation
import com.yuvarajcode.food_harbor.presentation.main.BottomNavigationScreens
import com.yuvarajcode.food_harbor.presentation.main.donation.user.DetailGrid
import com.yuvarajcode.food_harbor.presentation.main.donation.user.ProgressSteps
import com.yuvarajcode.food_harbor.presentation.main.donation.user.StatusChip
import com.yuvarajcode.food_harbor.utilities.ResponseState
import com.yuvarajcode.food_harbor.utilities.ToastForResponseState

@Composable
fun DonationOrganisationScreen(
    navController: NavController,
    donationOrgViewModel: DonationOrgViewModel
) {
    val donateeList by donationOrgViewModel.donateeList.collectAsState()
    val getDonationList by donationOrgViewModel.getDonationList.collectAsState()

    val donationList = when (getDonationList) {
        is ResponseState.Success -> (getDonationList as ResponseState.Success<List<Donation>>).data
        else -> emptyList()
    }

    when (val response = donateeList) {
        is ResponseState.Error -> {
            ToastForResponseState(message = response.message)
        }

        ResponseState.Loading -> {
            CircularProgressIndicator()
        }

        is ResponseState.Success -> {
            Log.d("DonationOrg", "Donation List: ${response.data}")
            Log.d("DonationOrg", "Donation List: ${donationList}")
            DonationOrgShowScreen(
                navController = navController,
                orgDonationList = response.data,
                donationOrgViewModel = donationOrgViewModel,
                donationList = donationList
            )
        }

        ResponseState.Initial -> {}
    }
}

@Composable
fun DonationOrgShowScreen(
    navController: NavController,
    orgDonationList: List<OrgDonation>,
    donationList: List<Donation>,
    donationOrgViewModel: DonationOrgViewModel,
) {

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("All", "Your Donations")

    Scaffold(
        topBar = { DonationOrgTopBar() },
        bottomBar = {
            BottomNavigation(
                selectedButton = BottomNavigationScreens.Donation,
                navController = navController
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            TabRow(
                selectedTabIndex = selectedTab
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 18.sp,
                                color = if (selectedTab == index) Color.Black else Color.Gray
                            )
                        }
                    )
                }
            }
            when (selectedTab) {
                0 -> {
                    DonationList(
                        donationList = donationList,
                        donationOrgViewModel = donationOrgViewModel
                    )
                }

                1 -> {
                    OrgDonationList(
                        orgDonationList = orgDonationList,
                        donationOrgViewModel = donationOrgViewModel
                    )
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
                text = "Donations",
                fontWeight = FontWeight.Bold,
            )
        },
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surface,
            navigationIconContentColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
            actionIconContentColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
            titleContentColor = Color(red = 7, green = 31, blue = 27, alpha = 255)
        )
    )
}


@Composable
fun DonationList(
    donationList: List<Donation>,
    donationOrgViewModel: DonationOrgViewModel
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(donationList) { donation ->
            DonationCard(
                donation = donation,
                donationOrgViewModel = donationOrgViewModel
            )
        }
    }
}


@Composable
fun OrgDonationList(
    orgDonationList: List<OrgDonation>,
    donationOrgViewModel: DonationOrgViewModel
) {
    LazyColumn {
        items(
            orgDonationList
        ) {
            OrgDonationCard(
                orgDonation = it,
                donationOrgViewModel = donationOrgViewModel
            )
        }
    }
}

data class OrgDonationUIState(
    val donation: ResponseState<Donation> = ResponseState.Initial,
    val receivedStatus: ResponseState<Boolean> = ResponseState.Initial,
    val transactionStatus: ResponseState<Boolean> = ResponseState.Initial,
    val removeDonation: ResponseState<Boolean> = ResponseState.Initial
)


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OrgDonationCard(
    orgDonation: OrgDonation,
    donationOrgViewModel: DonationOrgViewModel
) {
    val orgDonationState by donationOrgViewModel.getOrgDonationState(orgDonation.id)
        .collectAsState()
    LaunchedEffect(
        key1 = orgDonation.id
    ) {
        donationOrgViewModel.loadOrgDonationData(orgDonation.id)
    }

    val donation = when (val response = orgDonationState.donation) {
        is ResponseState.Success -> (response as ResponseState.Success<Donation>).data
        else -> Donation()
    }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    var moreDetailsExpanded by remember { mutableStateOf(false) }
    var acceptingOrgExpanded by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState(pageCount = { donation.imageUrl.size })
    var acceptedByExpanded by remember { mutableStateOf(false) }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding((screenHeight * 0.02).dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 4.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(modifier = Modifier.padding((screenHeight * 0.02).dp)) {
            // Image
            HorizontalPager(
                state = pagerState
            ) { page ->
                AsyncImage(
                    model = donation.imageUrl[page],
                    contentDescription = donation.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((screenHeight * 0.2).dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height((screenHeight * 0.0175).dp))

            // Title and Description
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = donation.name,
                    fontSize = (screenHeight * 0.0225).sp,
                    fontWeight = FontWeight.Bold
                )
                Log.d("DonationCard", "Status: $orgDonation.status")
                when (orgDonation.status) {
                    DonationStatus.PENDING.status -> StatusChip("Pending", Color(0xFFFFB74D))
                    DonationStatus.ACCEPTEDBYORG.status -> StatusChip(
                        "Accepted by NGO",
                        Color(0xFF4CAF50)
                    )

                    DonationStatus.REJECTEDBYDONOR.status -> StatusChip(
                        "Rejected by Donor",
                        Color(0xFFFFB74D)
                    )

                    DonationStatus.ACCEPTEDBYDONOR.status -> StatusChip(
                        "Accepted by Donor",
                        Color(0xFF4CAF50)
                    )

                    DonationStatus.DONATIONGIVEN.status -> StatusChip(
                        "Donation Given",
                        Color(0, 123, 255)
                    )

                    DonationStatus.DONATIONRECEIVED.status -> StatusChip(
                        "Donation Received",
                        Color(0, 123, 255)
                    )

                    DonationStatus.TRANSACTIONCOMPLETE.status -> StatusChip(
                        "Transaction Complete",
                        Color(23, 162, 184)
                    )
                }
            }
            Text(
                text = donation.description,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = (screenHeight * 0.0165).sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Details Grid
            DetailGrid(donation, screenHeight)

            Spacer(modifier = Modifier.height(12.dp))

            // Progress Steps
            ProgressSteps(
                currentStep = when (orgDonation.status) {
                    DonationStatus.PENDING.status -> 1
                    DonationStatus.ACCEPTEDBYORG.status -> 2
                    DonationStatus.ACCEPTEDBYDONOR.status -> 3
                    DonationStatus.DONATIONGIVEN.status -> 4
                    DonationStatus.DONATIONRECEIVED.status -> 5
                    DonationStatus.TRANSACTIONCOMPLETE.status -> 6
                    else -> 0
                }
            )


            when (orgDonationState.receivedStatus) {
                is ResponseState.Error -> {
                    ToastForResponseState(message = (orgDonationState.receivedStatus as ResponseState.Error).message)
                }

                ResponseState.Initial -> {

                }

                ResponseState.Loading -> {

                }

                is ResponseState.Success -> {
                    ToastForResponseState(message = "Donation Received")
                    donationOrgViewModel.updateOrgDonationStates(orgDonation.id) {
                        it.copy(receivedStatus = ResponseState.Initial)
                    }
                }
            }

            when (orgDonationState.transactionStatus) {
                is ResponseState.Error -> {
                    ToastForResponseState(message = (orgDonationState.transactionStatus as ResponseState.Error).message)
                }

                ResponseState.Initial -> {

                }

                ResponseState.Loading -> {

                }

                is ResponseState.Success -> {
                    ToastForResponseState(message = "Transaction Complete")
                    donationOrgViewModel.updateOrgDonationStates(orgDonation.id) {
                        it.copy(transactionStatus = ResponseState.Initial)
                    }
                }
            }

            when (orgDonationState.removeDonation) {
                is ResponseState.Error -> {
                    ToastForResponseState(message = (orgDonationState.removeDonation as ResponseState.Error).message)
                }

                ResponseState.Initial -> {

                }

                ResponseState.Loading -> {

                }

                is ResponseState.Success -> {
                    ToastForResponseState(message = "Donation Removed")
                    donationOrgViewModel.updateOrgDonationStates(orgDonation.id) {
                        it.copy(removeDonation = ResponseState.Initial)
                    }
                }
            }

            // Action Buttons

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = (screenHeight * 0.02).dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (orgDonation.status == DonationStatus.DONATIONGIVEN.status) {
                    Button(
                        onClick = {
                            donationOrgViewModel.receivedDonation(orgDonation.id)
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Received")
                    }
                }
                if (orgDonation.status == DonationStatus.DONATIONRECEIVED.status) {
                    Button(
                        onClick = {
                            donationOrgViewModel.transactionCompleteDonation(orgDonation.id)
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0, 123, 255),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Safe to Delete")
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                if (orgDonation.status != DonationStatus.DONATIONRECEIVED.status) {
                    OutlinedButton(
                        onClick = {
                            donationOrgViewModel.rejectDonateeStatus(orgDonation.id)
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Text("Remove donation")
                    }
                }

            }
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp)
            )

            TextButton(
                onClick = {
                    moreDetailsExpanded = !moreDetailsExpanded
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "View Details",
                    color = Color(0xFF4CAF50)
                )
            }
        }
    }
}


data class DonationOrgUIState(
    val acceptDonation: ResponseState<Boolean> = ResponseState.Initial,
    val rejectDonation: ResponseState<Boolean> = ResponseState.Initial,
    val updateStatus: ResponseState<Boolean> = ResponseState.Initial
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DonationCard(
    donation: Donation,
    donationOrgViewModel: DonationOrgViewModel,
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    var moreDetailsExpanded by remember { mutableStateOf(false) }
    var acceptingOrgExpanded by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState(pageCount = { donation.imageUrl.size })
    var acceptedByExpanded by remember { mutableStateOf(false) }

    val donationState by donationOrgViewModel.getDonationOrgState(donation.id).collectAsState()


    LaunchedEffect(
        donation.id
    ) {

    }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding((screenHeight * 0.02).dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 4.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(modifier = Modifier.padding((screenHeight * 0.02).dp)) {
            // Image
            HorizontalPager(
                state = pagerState
            ) { page ->
                AsyncImage(
                    model = donation.imageUrl[page],
                    contentDescription = donation.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((screenHeight * 0.2).dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height((screenHeight * 0.0175).dp))

            // Title and Description
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = donation.name,
                    fontSize = (screenHeight * 0.0225).sp,
                    fontWeight = FontWeight.Bold
                )
                Log.d("DonationCard", "Status: $donation.status")
                when (donation.status) {
                    DonationStatus.PENDING.status -> StatusChip("Pending", Color(0xFFFFB74D))
                    DonationStatus.ACCEPTEDBYORG.status -> StatusChip(
                        "Accepted by NGO",
                        Color(0xFF4CAF50)
                    )

                    DonationStatus.ACCEPTEDBYDONOR.status -> StatusChip(
                        "Accepted by Donor",
                        Color(0xFF4CAF50)
                    )

                    DonationStatus.DONATIONGIVEN.status -> StatusChip(
                        "Donation Given",
                        Color(0xFF4CAF50)
                    )

                    DonationStatus.TRANSACTIONCOMPLETE.status -> StatusChip(
                        "Transaction Complete",
                        Color(0xFF4CAF50)
                    )
                }
            }
            Text(
                text = donation.description,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = (screenHeight * 0.0165).sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Details Grid
            DetailGrid(donation, screenHeight)

            Spacer(modifier = Modifier.height(12.dp))

            // Progress Steps
            ProgressSteps(
                currentStep = when (donation.status) {
                    DonationStatus.PENDING.status -> 1
                    DonationStatus.ACCEPTEDBYORG.status -> 2
                    DonationStatus.ACCEPTEDBYDONOR.status -> 3
                    DonationStatus.DONATIONGIVEN.status -> 4
                    DonationStatus.DONATIONRECEIVED.status -> 5
                    DonationStatus.TRANSACTIONCOMPLETE.status -> 6
                    else -> 0
                }
            )


            when (donationState.acceptDonation) {
                is ResponseState.Error -> {
                    ToastForResponseState(message = (donationState.acceptDonation as ResponseState.Error).message)
                }

                ResponseState.Initial -> {

                }

                ResponseState.Loading -> {

                }

                is ResponseState.Success -> {
                    ToastForResponseState(message = "Donation Accepted")
                    donationOrgViewModel.updateDonationOrgStates(donation.id) {
                        it.copy(acceptDonation = ResponseState.Initial)
                    }
                }
            }

            when (donationState.rejectDonation) {
                is ResponseState.Error -> {
                    ToastForResponseState(message = (donationState.rejectDonation as ResponseState.Error).message)
                }

                ResponseState.Initial -> {

                }

                ResponseState.Loading -> {

                }

                is ResponseState.Success -> {
                    ToastForResponseState(message = "Donation Rejected")
                    donationOrgViewModel.updateDonationOrgStates(donation.id) {
                        it.copy(rejectDonation = ResponseState.Initial)
                    }
                }
            }


            // Action Buttons

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = (screenHeight * 0.02).dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (donation.status != DonationStatus.DONATIONGIVEN.status && donation.status != DonationStatus.TRANSACTIONCOMPLETE.status && donation.status != DonationStatus.EXPIRED.status && donation.status != DonationStatus.DONATIONRECEIVED.status) {
                    Button(
                        onClick = {
                            donationOrgViewModel.addDonateeToDonation(donation.id)
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Accept")
                    }
                }
            }



            HorizontalDivider(
                modifier = Modifier.padding(vertical = 4.dp)
            )

            TextButton(
                onClick = {
                    moreDetailsExpanded = !moreDetailsExpanded
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "View Details",
                    color = Color(0xFF4CAF50)
                )
            }

        }
    }
}

