package com.yuvarajcode.food_harbor.presentation.profile.main

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.yuvarajcode.food_harbor.domain.model.Donation
import com.yuvarajcode.food_harbor.presentation.authentication.AuthenticationViewModel
import com.yuvarajcode.food_harbor.presentation.profile.ProfileViewmodel
import com.yuvarajcode.food_harbor.shared.formatRelativeTime
import com.yuvarajcode.food_harbor.utilities.ResponseState
import com.yuvarajcode.food_harbor.utilities.Screens

object AppColors {
    val Green = Color(0xFF4CAF50)
    val LightGreen = Color(0xFFE8F5E9)
    val TextGray = Color(0xFF666666)
    val Background = Color(0xFFFAFAFA)
}

@Composable
fun ProfileOrgScreen(
    profileViewmodel: ProfileViewmodel,
    navController: NavController,
    authenticationViewModel: AuthenticationViewModel,
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background)
    ) {
        item {
            HeaderSection(
                onClick = {
                    navController.navigate(Screens.ProfileEditScreen.route)
                },
                name = profileViewmodel.realObj.userName,
                screenHeight = screenHeight,
                logoUrl = profileViewmodel.realObj.profilePictureUrl
            )
        }
        item {
            StatsSection(
                screenHeight = screenHeight,
                totalDonation = profileViewmodel.realObj.totalDonation,
                ngosDonated = profileViewmodel.realObj.ngosDonated
            )

        }
        item{
            GoalSection(
                screenHeight = screenHeight,
                goalTitle = "Weekly Goal",
                goal = profileViewmodel.realObj.weeklyGoal,
                totalDonation = profileViewmodel.realObj.totalDonation
            )
            GoalSection(
                screenHeight = screenHeight,
                goalTitle = "Monthly Goal",
                goal = profileViewmodel.realObj.monthlyGoal,
                totalDonation = profileViewmodel.realObj.totalDonation
            )
            GoalSection(
                screenHeight = screenHeight,
                goalTitle = "Yearly Goal",
                goal = profileViewmodel.realObj.yearlyGoal,
                totalDonation = profileViewmodel.realObj.totalDonation
            )
        }
        item {
            RecentDonationsSectionForOrg(
                screenHeight = screenHeight,
                profileViewmodel = profileViewmodel,
                onClick = {
                    navController.navigate(Screens.DonationHistoryScreen.route)
                }
            )
        }
        item { SignOutButton(authenticationViewModel, navController) }
    }
}

@Composable
fun HeaderSection(
    onClick: () -> Unit,
    name: String,
    logoUrl : String,
    screenHeight: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height((screenHeight * 0.25).dp)
    ) {
        // Background Image
        AsyncImage(
            model = "",
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.Green),
            contentScale = ContentScale.Crop,
        )

        // Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
        )

        // Edit Profile Button
        Button(
            onClick = {
                onClick()
            },
            modifier = Modifier
                .padding((screenHeight * 0.02).dp)
                .align(Alignment.TopEnd),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            )
        ) {
            Icon(
                Icons.Outlined.Edit,
                contentDescription = "Edit",
                tint = AppColors.Green
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                "Edit Profile",
                color = AppColors.Green
            )
        }

        // Foundation Info
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding((screenHeight * 0.02).dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AsyncImage(
                    model = logoUrl,
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size((screenHeight * 0.07).dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentScale = ContentScale.Crop
                )
                Column {
                    Text(
                        name,
                        color = Color.White,
                        fontSize = (screenHeight * 0.0225).sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Serving Mumbai Since 2024",
                        color = Color.White,
                        fontSize = (screenHeight * 0.02).sp
                    )
                }
            }
        }
    }
}

@Composable
private fun StatsSection(
    screenHeight: Int,
    totalDonation : Int,
    ngosDonated : Int
) {
    val co2Saved = (totalDonation * 2.5).toInt()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding((screenHeight * 0.02).dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StatItem(
            icon = Icons.Default.Favorite,
            value = totalDonation.toString(),
            label = "Total Donations"
        )
        StatItem(
            icon = Icons.Default.LocalShipping,
            value = co2Saved.toString(),
            label = "CO2 Saved (kg)"
        )
        StatItem(
            icon = Icons.Default.People,
            value = ngosDonated.toString(),
            label = "Active Donors"
        )
    }
}

@Composable
fun StatItem(icon: ImageVector, value: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = AppColors.Green,
            modifier = Modifier.size(24.dp)
        )
        Text(
            value,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Text(
            label,
            color = AppColors.TextGray,
            fontSize = 12.sp,
            lineHeight = 16.sp
        )
    }
}

@Composable
private fun RecentDonationsSectionForOrg(
    screenHeight: Int,
    profileViewmodel: ProfileViewmodel,
    onClick: () -> Unit
) {
    val getHistoryState by profileViewmodel.getHistoryOrg.collectAsState()

    val history = when(getHistoryState){
        is ResponseState.Success -> (getHistoryState as ResponseState.Success<List<Donation>>).data
        else -> emptyList()
    }
    Column(
        modifier = Modifier.padding((screenHeight * 0.02).dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Recent Donations",
                fontSize = (screenHeight * 0.0225).sp,
                fontWeight = FontWeight.Bold
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    onClick()
                }
            ) {
                Text(
                    "View All",
                    color = ProfileColors.Green
                )
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = "View All",
                    tint = ProfileColors.Green
                )
            }
        }

        Spacer(modifier = Modifier.height((screenHeight * 0.01).dp))

        if (history.isEmpty()) {
            Text(
                text = "No Donation History",
                fontSize = (screenHeight * 0.02).sp
            )
        } else {
            for (donation in history.take(3)) {
                DonationItem(
                    title = donation.name,
                    organization = donation.donateeName,
                    status = donation.status,
                    date = formatRelativeTime(donation.time.toLong())
                )
            }
        }
    }
}





//@Preview
//@Composable
//fun ProfileOrgScreenPreview() {
//    ProfileOrgScreen()
//}