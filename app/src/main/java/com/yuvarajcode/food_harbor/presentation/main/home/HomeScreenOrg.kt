package com.yuvarajcode.food_harbor.presentation.main.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.yuvarajcode.food_harbor.R
import com.yuvarajcode.food_harbor.domain.model.Donation
import com.yuvarajcode.food_harbor.presentation.profile.main.RecentDonationsSection
import com.yuvarajcode.food_harbor.utilities.ResponseState
import com.yuvarajcode.food_harbor.utilities.Screens

@Composable
fun HomeOrgScreen(
    navController: NavController,
    homeScreenViewModel: HomeScreenViewModel
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp

    Scaffold (
        topBar = {
            FoodShareTopBar(
                screenHeight = screenHeight
            )
        }
    ){
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .background(Color(249, 250, 251))
                .padding(horizontal = (screenHeight * 0.02).dp),
            verticalArrangement = Arrangement.spacedBy((screenHeight * 0.02).dp)
        ) {
            item { UrgentNeedCard(
                screenHeight = screenHeight
            ) }
            item { StatsRow(
                screenHeight = screenHeight,
                homeScreenViewModel.userData.totalDonation,
                homeScreenViewModel.userData.ngosDonated
            ) }
            item {
                RecentDonations(
                    screenHeight = screenHeight,
                    homeScreenViewModel = homeScreenViewModel
                )
            }
            item{
                YourImpact(
                    screenHeight = screenHeight,
                    homeScreenViewModel = homeScreenViewModel
                )
                Spacer(modifier = Modifier.height((screenHeight * 0.02).dp))
                QuickActions(
                    screenHeight = screenHeight,
                    onChatClick = {
                        navController.navigate(Screens.ChatScreen.route)
                    },
                    onHistoryClick = {
                        navController.navigate(Screens.DonationHistoryScreen.route)
                    }
                )
            }

        }

    }
}


@Composable
fun UrgentNeedCard(
    screenHeight: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height((screenHeight * 0.15).dp),
        shape = RoundedCornerShape((screenHeight * 0.02).dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF2196F3),
                            Color(0xFF4CAF50)
                        )
                    )
                )
                .padding((screenHeight * 0.025).dp)
        ) {
            Column (
                modifier = Modifier.align(Alignment.CenterStart)
            ){
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Column {
                        Text(
                            "Urgent: 100 meals needed today",
                            color = Color.White,
                            fontSize = (screenHeight * 0.025).sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            "New Donor Alert: 10kg veggies nearby!",
                            color = Color.White,
                            fontSize = (screenHeight * 0.015).sp
                        )
                    }
                }


            }
        }
    }
}

@Composable
fun StatsRow(
    screenHeight: Int,
    totalDonation: Int,
    userDonations : Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        StatCard(
            title = "Total Meals",
            value = "$totalDonation Kg",
            modifier = Modifier.weight(1f)
        )
        StatCard(
            title = "Active Donors",
            value = "$userDonations",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    modifier: Modifier
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    Card(
        modifier = modifier
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding((screenHeight * 0.02).dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                title,
                color = Color.Gray,
                fontSize = 14.sp
            )
            Text(
                value,
                fontSize = (screenHeight * 0.025).sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
fun PendingDonationsSection(
    screenHeight: Int
) {
    Column(
        verticalArrangement = Arrangement.spacedBy((screenHeight * 0.02).dp),
        modifier = Modifier.padding(vertical = (screenHeight * 0.02).dp)
    ) {
        Text(
            "Pending Donations",
            fontSize = (screenHeight * 0.0275).sp,
            fontWeight = FontWeight.Bold
        )

        PendingDonationItem(
            name = "Sarah Johnson",
            type = "Cooked Meals",
            time = "Today, 2:00 PM",
            address = "123 Park Avenue, NY"
        )

        PendingDonationItem(
            name = "Michael Chen",
            type = "Fresh Vegetables",
            time = "Today, 4:30 PM",
            address = "456 Oak Street, NY"
        )

        PendingDonationItem(
            name = "Emily Davis",
            type = "Bakery Items",
            time = "Tomorrow, 9:00 AM",
            address = "789 Maple Road, NY"
        )
    }
}

@Composable
fun DonationItem(name: String, description: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AsyncImage(
            model = "https://placeholder.com/48",
            contentDescription = "Donor",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
        )
        Column {
            Text(
                name,
                fontWeight = FontWeight.Bold
            )
            Text(
                description,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun PendingDonationItem(
    name: String,
    type: String,
    time: String,
    address: String
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding((screenHeight * 0.02).dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Donor",
                        modifier = Modifier
                            .size((screenHeight * 0.05).dp)
                            .clip(CircleShape)
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            name,
                            fontWeight = FontWeight.Bold,
                            fontSize = (screenHeight * 0.0175).sp
                        )
                        Text(
                            type,
                            color = Color(75,85,99)
                        )
                    }
                }

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFF3E0)
                    ),
                    shape = RoundedCornerShape((screenHeight * 0.02).dp)
                ) {
                    Text(
                        "Pending",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        color = Color(0xFFFF9800),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Schedule,
                    contentDescription = null,
                    tint = Color(75,85,99),
                    modifier = Modifier.size((screenHeight * 0.02).dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(time, color = Color(75,85,99), fontSize = 14.sp)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(75,85,99),
                    modifier = Modifier.size((screenHeight * 0.02).dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(address, color = Color(75,85,99), fontSize = 14.sp)
            }
        }
    }
}



//
//@Preview(showBackground = true)
//@Composable
//fun PreviewFoodShareScreen() {
//    HomeOrgScreen()
//}
