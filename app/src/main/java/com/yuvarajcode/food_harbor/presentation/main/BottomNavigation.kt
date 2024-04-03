package com.yuvarajcode.food_harbor.presentation.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.twotone.Menu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yuvarajcode.food_harbor.utilities.Screens

enum class BottomNavigationScreens(val route: String,val icon:ImageVector) {
    Home(Screens.HomeScreen.route,Icons.Outlined.Home),
    Profile(Screens.ProfileStateScreen.route,Icons.Outlined.AccountCircle),
    News(Screens.NewsScreen.route,Icons.AutoMirrored.Default.List),
    Organisation(Screens.OrganisationScreen.route,Icons.TwoTone.Menu),
    Donation(Screens.DonationScreen.route,Icons.Rounded.Favorite)
}

@Composable
fun BottomNavigation(
    selectedButton : BottomNavigationScreens,
    navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = Color(red = 7, green = 31, blue = 27, alpha = 255)),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        for (item in BottomNavigationScreens.entries) {
            Column {
                Image(
                    imageVector = item.icon,
                    contentDescription = item.name,
                    modifier = Modifier
                        .clickable {
                            navController.navigate(item.route)
                        }
                        .padding(8.dp)
                        .padding(top = 8.dp)
                        .size(24.dp)
                        .align(Alignment.CenterHorizontally),
                    colorFilter = if (selectedButton == item) {
                        ColorFilter.tint(Color.White)
                    } else {
                        ColorFilter.tint(Color.Gray)
                    }
                )
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = if (selectedButton == item) {
                        Color.White
                    } else {
                        Color.Gray
                    },
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}