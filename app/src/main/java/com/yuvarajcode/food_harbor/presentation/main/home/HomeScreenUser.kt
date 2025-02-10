package com.yuvarajcode.food_harbor.presentation.main.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.yuvarajcode.food_harbor.R
import com.yuvarajcode.food_harbor.domain.model.Donation
import com.yuvarajcode.food_harbor.utilities.ResponseState
import com.yuvarajcode.food_harbor.utilities.Screens

@Composable
fun HomeUserScreen(
    navController: NavController,
    homeScreenViewModel: HomeScreenViewModel
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    Scaffold(
        topBar = {
            FoodShareTopBar(
                screenHeight = screenHeight
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(249, 250, 251))
                .verticalScroll(rememberScrollState())
                .padding(horizontal = (screenHeight * 0.02).dp)
        ) {
            DonationBanner(
                screenHeight = screenHeight,
            )
            Spacer(modifier = Modifier.height((screenHeight * 0.03).dp))
            RecentDonations(
                screenHeight = screenHeight,
                homeScreenViewModel = homeScreenViewModel
            )
            Spacer(modifier = Modifier.height((screenHeight * 0.02).dp))
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
            Spacer(modifier = Modifier.height((screenHeight * 0.02).dp))
            YourAchievements(
                screenHeight = screenHeight
            )
            Spacer(modifier = Modifier.height((screenHeight * 0.02).dp))
            QuickDonateButton(
                screenHeight = screenHeight,
                onClick = {
                    navController.navigate(Screens.DonationFormScreen.route)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodShareTopBar(
    screenHeight: Int
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Food-Harbor",
                    modifier = Modifier
                        .size((screenHeight * 0.035).dp)
                        .clip(CircleShape),
                )
                Text(
                    text = "Food-Harbor",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        },

        )
}

@Composable
fun DonationBanner(
    screenHeight: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height((screenHeight * 0.15).dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF4CAF50),
                        Color(0xFF81C784)
                    )
                )
            )
            .padding((screenHeight * 0.025).dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Text(
                text = "Turn Surplus into Smiles",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Your kindness makes a difference",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

data class Donor(
    val name: String,
    val donation: String,
    val imageId: Int
)


data class DonationListUIState(
    val profilePic : ResponseState<String> = ResponseState.Initial,
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecentDonations(
    screenHeight: Int,
    homeScreenViewModel: HomeScreenViewModel
) {
    val donationListState by homeScreenViewModel.getDonateList.collectAsState()

    LaunchedEffect(
        key1 = Unit
    ) {
        homeScreenViewModel.getDonationList()
    }
    val donationList = when (donationListState) {
        is ResponseState.Success -> {
            (donationListState as ResponseState.Success<List<Donation>>).data
        }

        else -> {
            emptyList()
        }
    }

    Text(
        text = "Recent Donations",
        fontSize = (screenHeight * 0.0275).sp,
        fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(8.dp))

    if (donationList.isEmpty()) {
        Text(
            text = "No Donations available",
            style = MaterialTheme.typography.headlineMedium
        )
    } else {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = (screenHeight * 0.02).dp),
        ) {
            items(donationList.size) { index ->
                val donation = donationList[index]
                DonorProfile(
                    homeScreenViewModel = homeScreenViewModel,
                    donation = donation,
                    screenHeight = screenHeight
                )
            }
        }
    }
}

@Composable
fun DonorProfile(
    homeScreenViewModel: HomeScreenViewModel,
    donation: Donation,
    screenHeight: Int
) {
    val donationListState by homeScreenViewModel.getDonationListState(donationId = donation.id).collectAsState()

    LaunchedEffect(
        key1 = donation.id
    ) {
        homeScreenViewModel.loadDonationListState(donationId = donation.id, userId = donation.userId)
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = (screenHeight * 0.02).dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding((screenHeight * 0.02).dp)
        ) {
            AsyncImage(
                model = when(donationListState.profilePic){
                    is ResponseState.Success -> {
                        (donationListState.profilePic as ResponseState.Success<String>).data
                    }
                    else -> {
                        ""
                    }
                },
                contentDescription = donation.name,
                modifier = Modifier
                    .size((screenHeight * 0.05).dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = donation.username, fontWeight = FontWeight.Bold)
                if (donation.name.isNotEmpty()) {
                    Text(text = donation.name, style = MaterialTheme.typography.bodySmall)
                }
            }
            Spacer(modifier = Modifier.width((screenHeight * 0.07).dp))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun YourImpact(
    screenHeight: Int,
    homeScreenViewModel: HomeScreenViewModel
) {
    val pager = rememberPagerState { 3 }
    val totalDonation = homeScreenViewModel.userData.totalDonation
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = (screenHeight * 0.01).dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier.padding((screenHeight * 0.02).dp)
        ) {
            Text(
                text = "Your Impact",
                fontSize = (screenHeight * 0.0275).sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height((screenHeight * 0.02).dp))
            HorizontalPager(
                state = pager,
            ) {page->
                val goalValue = when(page){
                    0 -> homeScreenViewModel.userData.weeklyGoal
                    1 -> homeScreenViewModel.userData.monthlyGoal
                    2 -> homeScreenViewModel.userData.yearlyGoal
                    else -> {
                        0
                    }
                }
                val type = when(page){
                    0 -> "Weekly"
                    1 -> "Monthly"
                    2 -> "Yearly"
                    else -> {
                        ""
                    }
                }
                ImpactProgressCard(
                    screenHeight = screenHeight,
                    totalDonation = totalDonation,
                    goalValue = goalValue,
                    type = type
                )
            }
        }
    }
}

@Composable
fun ImpactProgressCard(
    screenHeight: Int,
    totalDonation: Int,
    goalValue: Int,
    type : String
){
    val progress = if (goalValue != 0) {
        (totalDonation.toFloat() / goalValue.toFloat()).coerceIn(0f, 1f)
    } else {
        0f
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = (screenHeight * 0.02).dp).padding(horizontal = (screenHeight * 0.02).dp)
    ) {
        Box(
            modifier = Modifier
                .size((screenHeight * 0.09).dp)
                .clip(CircleShape)
                .background(Color(0xFF4CAF50)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${totalDonation.toString()}Kg",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )
        }
        Spacer(modifier = Modifier.width((screenHeight * 0.02).dp))
        Column {
            Text(
                text = "$type Goal: $goalValue kg",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth(0.7f),
                color = Color(0xFF4CAF50),
            )
        }
    }
}


@Composable
fun QuickActions(
    screenHeight: Int,
    onChatClick: () -> Unit,
    onHistoryClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        QuickActionButton(
            icon = Icons.Default.ChatBubble,
            text = "Your Chat",
            onClick = onChatClick
        )
        QuickActionButton(
            icon = Icons.Default.List,
            text = "History",
            onClick = onHistoryClick
        )
    }
}

@Composable
fun QuickActionButton(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size((screenHeight * 0.06).dp)
                .clickable { onClick() }
                .clip(CircleShape)
                .background(Color(0xFFE8F5E9)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = Color(0xFF4CAF50)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun YourAchievements(
    screenHeight: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = (screenHeight * 0.02).dp),
    ) {
        Text(
            text = "Your Achievements",
            fontSize = (screenHeight * 0.0275).sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height((screenHeight * 0.02).dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AchievementBadge(text = "First Time Donor")
            AchievementBadge(text = "Weekly Streak")
            AchievementBadge(text = "Local Hero")
        }
    }
}

@Composable
fun AchievementBadge(text: String) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    Card(
        modifier = Modifier
            .width((screenHeight * 0.14).dp)
            .padding(top = (screenHeight * 0.01).dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier.padding((screenHeight * 0.0175).dp)
        ) {
            Box(
                modifier = Modifier
                    .size((screenHeight * 0.07).dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFA500)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = text,
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.height((screenHeight * 0.0175).dp))
            Text(
                text = text,
                fontSize = (screenHeight * 0.0175).sp,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Clip
            )

            LinearProgressIndicator(
                progress = 0.5f,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(top = (screenHeight * 0.02).dp),
                color = Color(0xFFFFA500)
            )
        }
    }
}

@Composable
fun QuickDonateButton(
    screenHeight: Int,
    onClick: () -> Unit
) {
    Button(
        onClick = {
            onClick()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height((screenHeight * 0.08).dp)
            .padding(vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF4CAF50)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = "Quick Donate",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White
        )
    }
}