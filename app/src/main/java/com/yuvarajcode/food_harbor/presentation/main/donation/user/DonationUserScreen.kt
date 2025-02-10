package com.yuvarajcode.food_harbor.presentation.main.donation.user

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.maryamrzdh.stepper.Stepper
import com.yuvarajcode.food_harbor.R
import com.yuvarajcode.food_harbor.domain.model.Donation
import com.yuvarajcode.food_harbor.domain.model.DonationStatus
import com.yuvarajcode.food_harbor.domain.model.User
import com.yuvarajcode.food_harbor.presentation.main.BottomNavigation
import com.yuvarajcode.food_harbor.presentation.main.BottomNavigationScreens
import com.yuvarajcode.food_harbor.utilities.ResponseState
import com.yuvarajcode.food_harbor.utilities.Screens
import com.yuvarajcode.food_harbor.utilities.ToastForResponseState
import kotlinx.coroutines.NonDisposableHandle.parent


data class DonationUIState(
    val status: ResponseState<String> = ResponseState.Initial,
    val getUsername: ResponseState<List<Pair<String,String>>> = ResponseState.Initial,
    val donateStatus: ResponseState<Boolean> = ResponseState.Initial,
    val transactionStatus : ResponseState<Boolean> = ResponseState.Initial,
)

data class DonateeUIState(
    val acceptStatus: ResponseState<Boolean> = ResponseState.Initial,
    val rejectStatus: ResponseState<Boolean> = ResponseState.Initial,
    val user: ResponseState<User> = ResponseState.Initial,
    val channelState : ResponseState<Boolean> = ResponseState.Initial
)


@Composable
fun DonationUserScreen(
    navController: NavController,
    donationScreenViewModel: DonationUserScreenViewModel
) {
    val donationList by donationScreenViewModel.getDonations.collectAsState()
    when (val response = donationList) {
        is ResponseState.Error -> {
            ToastForResponseState(message = response.message)
            Log.d("DonationScreen", "DonationScreenError: ${response.message}")
        }

        is ResponseState.Loading ->
            CircularProgressIndicator()

        is ResponseState.Success -> {
            Log.d("DonationScreen", "DonationScreenSuccess: $donationList")
            DonationShowScreen(
                navController = navController,
                donationList = response.data,
                donationScreenViewModel = donationScreenViewModel,
            )
        }

        ResponseState.Initial -> {

        }
    }
}

@Composable
fun DonationShowScreen(
    navController: NavController,
    donationList: List<Donation>,
    donationScreenViewModel: DonationUserScreenViewModel,
) {
    Scaffold(
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
                    imageVector = Icons.Default.Add,
                    contentDescription = "Adding a donation"
                )
            }
        },
        topBar = {
            DonationTopBar()
        }
    ) {
        Box(
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
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(donationList) { donation ->
                        DonationCard(
                            donation = donation,
                            donationUserScreenViewModel = donationScreenViewModel,
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
                text = "My Donations",
                fontSize = 30.sp,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonateeButton(
    orgId: String,
    donationUserScreenViewModel: DonationUserScreenViewModel,
    donationId: String,
) {
    val context = LocalContext.current
    val donateeState =
        donationUserScreenViewModel.getDonateeState(orgId, donationId).collectAsState()

    val channelState by donationUserScreenViewModel.channelCreate
    LaunchedEffect(
        key1 = orgId
    ) {
        donationUserScreenViewModel.loadDonateeData(donationId = donationId, donateeId = orgId)
    }
    val user = when (donateeState.value.user) {
        is ResponseState.Success -> (donateeState.value.user as ResponseState.Success).data
        else -> User()
    }

    Row {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .weight(0.7f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = user.profilePictureUrl,
                contentDescription = user.name,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                placeholder = painterResource(id = R.drawable.ic_broken_image),
                contentScale = ContentScale.Crop
            )
            Text(
                text = user.name,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Row {
            IconButton(
                onClick = {
                    donationUserScreenViewModel.acceptDonateeStatus(
                        user.userId,
                        DonationStatus.ACCEPTEDBYDONOR.status,
                        donationId
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Accept"
                )
            }
            IconButton(
                onClick = {
                    donationUserScreenViewModel.rejectDonateeStatus(
                        user.userId,
                        DonationStatus.REJECTEDBYDONOR.status,
                        donationId
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Reject"
                )
            }

        }


    }


    LaunchedEffect(key1 = donateeState.value.acceptStatus) {
        when (donateeState.value.acceptStatus) {
            is ResponseState.Error -> {
                Toast.makeText(
                    context,
                    (donateeState.value.acceptStatus as ResponseState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            ResponseState.Initial -> {

            }

            ResponseState.Loading -> TODO()
            is ResponseState.Success -> {
                Toast.makeText(
                    context,
                    "Donatee status accepted successfully",
                    Toast.LENGTH_SHORT
                ).show()
                donationUserScreenViewModel.createChannel(orgId, donationId)
                donationUserScreenViewModel.updateDonateeState(orgId, donationId) {
                    it.copy(acceptStatus = ResponseState.Initial)
                }

            }
        }
    }

    LaunchedEffect(key1 = donateeState.value.rejectStatus) {
        when (donateeState.value.rejectStatus) {
            is ResponseState.Error -> {
                Toast.makeText(
                    context,
                    (donateeState.value.rejectStatus as ResponseState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            ResponseState.Initial -> {

            }

            ResponseState.Loading -> TODO()
            is ResponseState.Success -> {
                Toast.makeText(
                    context,
                    "Donatee status rejected successfully",
                    Toast.LENGTH_SHORT
                ).show()
                donationUserScreenViewModel.updateDonateeState(orgId, donationId) {
                    it.copy(rejectStatus = ResponseState.Initial)
                }
            }
        }
    }


    LaunchedEffect(key1 = donateeState.value.channelState) {
        when (val response = donateeState.value.channelState) {
            is ResponseState.Error -> {
                Toast.makeText(
                    context,
                    (response as ResponseState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            is ResponseState.Loading -> {
            }

            is ResponseState.Success -> {
                Toast.makeText(
                    context,
                    "Channel created successfully",
                    Toast.LENGTH_SHORT
                ).show()
                donationUserScreenViewModel.updateDonateeState(orgId, donationId) {
                    it.copy(channelState = ResponseState.Initial)
                }
            }

            ResponseState.Initial -> {

            }
        }
    }

}

@Composable
private fun OrganizationItem(
    name : String,
    logoUrl : String,
    selected: Boolean,
    onSelect: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect)
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        RadioButton(
            selected = selected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DonationCard(
    donation: Donation,
    donationUserScreenViewModel: DonationUserScreenViewModel,
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    var moreDetailsExpanded by remember { mutableStateOf(false) }
    var acceptingOrgExpanded by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState(pageCount = { donation.imageUrl.size })
    var acceptedByExpanded by remember { mutableStateOf(false) }
    var selectedUsername by remember { mutableStateOf("") }
    val context = LocalContext.current

    val donationState = donationUserScreenViewModel.getDonationState(donation.id).collectAsState()

    LaunchedEffect(
        donation.id
    ) {
        donationUserScreenViewModel.loadDonationData(donation.id, donation.userId)
    }

    val status = when (donationState.value.status) {
        is ResponseState.Success -> (donationState.value.status as ResponseState.Success).data
        else -> ""
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
                Log.d("DonationCard", "Status: $status")
                when (status) {
                    DonationStatus.PENDING.status -> StatusChip("Pending", Color(0xFFFFB74D))
                    DonationStatus.ACCEPTEDBYORG.status -> StatusChip(
                        "Accepted by NGO",
                        Color(40, 167, 69)
                    )

                    DonationStatus.ACCEPTEDBYDONOR.status -> StatusChip(
                        "Accepted by Donor",
                        Color(40, 167, 69)
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

            LaunchedEffect(key1 = donationState.value.donateStatus) {
                when (donationState.value.donateStatus) {
                    is ResponseState.Error -> {
                       Toast.makeText(
                            context,
                            (donationState.value.donateStatus as ResponseState.Error).message,
                            Toast.LENGTH_SHORT
                       ).show()
                    }

                    ResponseState.Initial -> {

                    }

                    ResponseState.Loading -> TODO()
                    is ResponseState.Success -> {
                        Toast.makeText(
                            context,
                            "Donation status updated successfully",
                            Toast.LENGTH_SHORT
                        ).show()
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
                if (status == DonationStatus.ACCEPTEDBYDONOR.status) {
                    Button(
                        onClick = {
                            acceptedByExpanded = true
                            donationUserScreenViewModel.getUsernamesByUserIds(
                                donation.donateeList,
                                donation.id
                            )
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Donate")
                    }
                }
                if(status == DonationStatus.DONATIONRECEIVED.status){
                    Button(
                        onClick = {
                            donationUserScreenViewModel.transactionCompleteDonation(donation.id, donation.acceptedBy)
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0,123,255),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Safe to Delete")
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                if (status != DonationStatus.DONATIONGIVEN.status) {
                    OutlinedButton(
                        onClick = {
                            donationUserScreenViewModel.deleteDonation(donation.id)
                            donationUserScreenViewModel.deleteDonationState(donation.id)
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Text("Remove donation")
                    }
                }
            }

            val usernames = when(donationState.value.getUsername){
                is ResponseState.Success -> (donationState.value.getUsername as ResponseState.Success).data
                else -> emptyList()
            }


            if (donation.donateeList.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Accepting organizations (${donation.donateeList.size})",
                        fontSize = (screenHeight * 0.02).sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(
                        onClick = {
                            acceptingOrgExpanded = !acceptingOrgExpanded
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Expand"
                        )
                    }
                }
            }

            if (acceptingOrgExpanded) {
                donation.donateeList.forEach {
                    DonateeButton(
                        orgId = it,
                        donationUserScreenViewModel = donationUserScreenViewModel,
                        donationId = donation.id,
                    )
                }
            }

            if(acceptedByExpanded){
                BasicAlertDialog(
                    onDismissRequest = { acceptedByExpanded = false },
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(28.dp)
                            )
                            .padding(vertical = 24.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Select Organization",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            IconButton(onClick = {acceptedByExpanded = false}) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close"
                                )
                            }
                        }

                        LazyColumn {
                            items(usernames){username ->
                                OrganizationItem(
                                    name = username.second,
                                    logoUrl = "",
                                    selected = selectedUsername == username.second,
                                    onSelect = {
                                        selectedUsername = username.second
                                    }
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextButton(
                                onClick = {
                                    acceptedByExpanded = false
                                }
                            ) {
                                Text("Cancel")
                            }
                            TextButton(
                                onClick = {
                                    donationUserScreenViewModel.updateDonationStatus(
                                        donation.id,
                                        DonationStatus.DONATIONGIVEN.status,
                                        usernames.find { it.second == selectedUsername }?.first ?: ""
                                    )
                                    acceptedByExpanded = false
                                },
                                enabled = selectedUsername.isNotEmpty()
                            ) {
                                Text("Donate")
                            }
                        }
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

@Composable
fun StatusChip(text: String, color: Color) {
    Surface(
        modifier = Modifier.padding(bottom = 8.dp),
        shape = RoundedCornerShape(16.dp),
        color = color.copy(alpha = 0.2f)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            color = color,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun DetailGrid(donation: Donation, screenHeight: Int) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(0.9f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DetailItem(
                icon = android.R.drawable.ic_menu_gallery,
                label = "Quantity",
                value = "${donation.quantity} Kg"
            )
            DetailItem(
                icon = android.R.drawable.ic_menu_my_calendar,
                label = "Date",
                value = donation.entryDate
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(0.9f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DetailItem(
                icon = android.R.drawable.ic_menu_mylocation,
                label = "Location",
                value = donation.location.substringAfterLast(" ")
            )
            DetailItem(
                icon = android.R.drawable.ic_menu_my_calendar,
                label = "expiry",
                value = donation.expiryDate
            )
        }
    }
}

@Composable
fun DetailItem(icon: Int, label: String, value: String) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = label,
            modifier = Modifier.size((screenHeight * 0.025).dp),
            tint = Color(33, 150, 243)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = label,
                fontSize = (screenHeight * 0.0175).sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                fontSize = (screenHeight * 0.02).sp,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun ProgressSteps(currentStep: Int) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    val numberOfSteps = 6
    Stepper(
        numberOfSteps = numberOfSteps,
        currentStep = currentStep,
        selectedColor = Color(0xFF4CAF50),
        unSelectedColor = Color(0xFFE0E0E0),
        stepDescriptionList = listOf(
            DonationStatus.PENDING.status,
            DonationStatus.ACCEPTEDBYORG.value ?: "",
            DonationStatus.ACCEPTEDBYDONOR.value ?: "",
            DonationStatus.DONATIONGIVEN.value ?: "",
            DonationStatus.DONATIONRECEIVED.value ?: "",
            DonationStatus.TRANSACTIONCOMPLETE.value ?: ""
        ),
        modifier = Modifier
            .fillMaxWidth()
    )
}

enum class DonationProgress(val text: String) {
    DONATED("Donated"),
    NGO_REVIEW("NGO Review"),
    ACCEPTED("Accepted")
}


//@Preview
//@Composable
//fun PreviewDonation() {
//    DonationCard(
//        donation = Donation(
//            id = "1",
//            userId = "1",
//            name = "Fresh vegetable Bundle",
//            description = "A fresh selection of vegetables including carrots, tomatoes and potatoes",
//            quantity = 10.0,
//            imageUrl = listOf("https://dpppsusunaiifqhubaka.supabase.co/storage/v1/object/public/video_images/videos_Title 6_1738160810216.jpg",
//                "https://dpppsusunaiifqhubaka.supabase.co/storage/v1/object/public/video_images/videos_Title 2_1736699740238.jpg"
//            ),
//            location = "Chennai",
//            contact = "1234567890",
//            entryDate = "2022-10-10",
//            expiryDate = "2022-10-10",
//            time = "10:00",
//            status = DonationStatus.ACCEPTEDBYORG.status,
//            donateeList = emptyList(),
//            acceptedBy = ""
//        )
//    )
//}
