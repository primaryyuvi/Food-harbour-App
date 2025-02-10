package com.yuvarajcode.food_harbor.presentation.profile.main

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.yuvarajcode.food_harbor.presentation.authentication.AuthenticationViewModel
import com.yuvarajcode.food_harbor.presentation.profile.ProfileViewmodel
import com.yuvarajcode.food_harbor.shared.formatRelativeTime
import com.yuvarajcode.food_harbor.utilities.ResponseState
import com.yuvarajcode.food_harbor.utilities.Screens

object ProfileColors {
    val Green = Color(46, 125, 50)
    val LightGreen = Color(0xFFE8F5E9)
    val PeachBg = Color(0xFFFFF3E0)
    val LightBlue = Color(0xFFE3F2FD)
    val TextGray = Color(0xFF666666)
}

@Composable
fun ProfileUserScreen(
    authenticationViewModel: AuthenticationViewModel,
    navController: NavController,
    profileViewmodel: ProfileViewmodel
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                ProfileHeader(
                    screenHeight = screenHeight,
                    onClick = {
                        navController.navigate(Screens.ProfileEditScreen.route)
                    },
                    logoUrl = profileViewmodel.realObj.profilePictureUrl,
                    name = profileViewmodel.realObj.userName
                )
                StatsSection(
                    screenHeight = screenHeight,
                    totalDonation = profileViewmodel.realObj.totalDonation,
                    userDonated = profileViewmodel.realObj.ngosDonated
                )
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
                AchievementsSection(
                    screenHeight = screenHeight
                )
                RecentDonationsSection(
                    screenHeight = screenHeight,
                    profileViewmodel = profileViewmodel,
                    onClick =
                    {
                        navController.navigate(Screens.DonationHistoryScreen.route)
                    }
                )
                SignOutButton(authenticationViewModel,navController)
            }
        }
    }
}

@Composable
fun ProfileHeader(
    screenHeight: Int,
    onClick: () -> Unit,
    logoUrl : String,
    name : String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ProfileColors.LightGreen)
            .padding((screenHeight * 0.02).dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = (screenHeight * 0.0225).dp),
            horizontalArrangement = Arrangement.End
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.clickable {
                    onClick()
                }
            ) {
                Icon(
                    Icons.Outlined.Edit,
                    contentDescription = "Edit",
                    tint = ProfileColors.Green
                )
                Text(
                    "Edit Profile",
                    color = ProfileColors.Green
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                AsyncImage(
                    model = logoUrl,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size((screenHeight * 0.07).dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "Verified",
                    tint = ProfileColors.Green,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size((screenHeight * 0.025).dp)
                        .background(Color.White, CircleShape)
                )
            }

            Spacer(modifier = Modifier.height((screenHeight * 0.02).dp))

            Text(
                name,
                fontSize = (screenHeight * 0.025).sp,
                fontWeight = FontWeight.Bold
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = (screenHeight * 0.01).dp)
            ) {
                Text(
                    "Food Hero Since 2024",
                    color = ProfileColors.Green
                )
            }
        }
    }
}

@Composable
private fun StatsSection(
    screenHeight: Int,
    totalDonation : Int,
    userDonated : Int,
) {
    val co2Saved = (totalDonation * 2.5).toInt()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = (screenHeight * 0.02).dp)
            .padding(horizontal = (screenHeight * 0.0125).dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StatCard(
            value = totalDonation.toString(),
            label = "kg Donated",
            backgroundColor = ProfileColors.LightGreen,
            icon = Icons.Default.Restaurant
        )
        StatCard(
            value = co2Saved.toString(),
            label = "kg CO₂ Saved",
            backgroundColor = ProfileColors.PeachBg,
            icon = Icons.Default.Eco
        )
        StatCard(
            value = userDonated.toString(),
            label = "NGOs Helped",
            backgroundColor = ProfileColors.LightBlue,
            icon = Icons.Default.Favorite
        )
    }
}

@Composable
fun StatCard(
    value: String,
    label: String,
    backgroundColor: Color,
    icon: ImageVector
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    Card(
        modifier = Modifier
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier.padding((screenHeight * 0.02).dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = ProfileColors.Green,
                modifier = Modifier.size((screenHeight * 0.03).dp)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 8.dp),
            ) {
                Text(
                    value,
                    fontSize = (screenHeight * 0.0225).sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    label,
                    fontSize = 12.sp,
                    color = ProfileColors.TextGray,
                    lineHeight = 14.sp
                )
            }

        }
    }
}

@Composable
fun GoalSection(
    screenHeight: Int,
    goal : Int = 100,
    totalDonation : Int,
    goalTitle : String
) {
    val progress = if (goal != 0) {
        (totalDonation.toFloat() / goal.toFloat()).coerceIn(0f, 1f)
    } else {
        0f
    }
    Log.d("GoalSection", "progress: $progress")
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = (screenHeight * 0.02).dp)
            .padding(bottom = (screenHeight * 0.015).dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 1.dp,
        )
    ) {
        Column(
            modifier = Modifier.padding((screenHeight * 0.02).dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    goalTitle,
                    fontSize = (screenHeight * 0.02).sp,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    "${totalDonation}/$goal kg",
                    color = ProfileColors.Green,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = ProfileColors.Green,
                trackColor = ProfileColors.LightGreen,
            )
        }
    }
}

@Composable
fun AchievementsSection(
    screenHeight: Int
) {
    Column(
        modifier = Modifier.padding((screenHeight * 0.02).dp)
    ) {
        Text(
            "Your Achievements",
            fontSize = (screenHeight * 0.0225).sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height((screenHeight * 0.02).dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AchievementItem("First Donation", ProfileColors.PeachBg, Icons.Default.Star)
            AchievementItem("Zero Waste Hero", ProfileColors.LightGreen, Icons.Default.Eco)
            AchievementItem("Regular Donor", ProfileColors.LightBlue, Icons.Default.CheckCircle)
            AchievementItem("Champion", Color(0xFFFFF3E0), Icons.Default.EmojiEvents)
        }
    }
}

@Composable
fun AchievementItem(
    label: String,
    backgroundColor: Color,
    icon: ImageVector
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size((screenHeight * 0.07).dp)
                .background(backgroundColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = label,
                tint = ProfileColors.Green
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            label,
            fontSize = 12.sp,
            color = ProfileColors.TextGray,
            lineHeight = 14.sp
        )
    }
}

@Composable
fun RecentDonationsSection(
    screenHeight: Int,
    profileViewmodel: ProfileViewmodel,
    onClick: () -> Unit
) {
    val getHistoryState by profileViewmodel.getHistory.collectAsState()

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
                fontSize = 16.sp
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

//        DonationItem(
//            title = "Fresh Produce",
//            organization = "Food Bank NYC",
//            status = "Pending",
//            date = "Today"
//        )
//
//        DonationItem(
//            title = "Canned Goods",
//            organization = "Helping Hands",
//            status = "Delivered",
//            date = "Yesterday"
//        )
//
//        DonationItem(
//            title = "Bakery Items",
//            organization = "Community Kitchen",
//            status = "Delivered",
//            date = "3 days ago"
//        )
    }
}

@Composable
fun DonationItem(
    title: String,
    organization: String,
    status: String,
    date: String
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    Card (
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 0.5.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = (screenHeight * 0.02).dp, horizontal = (screenHeight * 0.02).dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(ProfileColors.Green, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Restaurant,
                        contentDescription = null,
                        tint = Color.White
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Text(
                        title,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                    Text(
                        organization,
                        color = ProfileColors.TextGray,
                        fontSize = 12.sp
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    date,
                    color = ProfileColors.TextGray,
                    fontSize = 12.sp
                )
                Text(
                    status,
                    color = if (status == "Pending") Color(0xFFFF9800) else ProfileColors.Green,
                    fontSize = 12.sp
                )
            }
        }
    }
}
