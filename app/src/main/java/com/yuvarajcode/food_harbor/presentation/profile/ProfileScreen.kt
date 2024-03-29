package com.yuvarajcode.food_harbor.presentation.profile

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.yuvarajcode.food_harbor.presentation.main.BottomNavigation
import com.yuvarajcode.food_harbor.presentation.main.BottomNavigationScreens

@Composable
fun ProfileScreen(
    navController: NavController
) {
    Scaffold(
        bottomBar = {
            BottomNavigation(
                selectedButton = BottomNavigationScreens.Profile,
                navController = navController
            )
        }
    ) {
        Text(
            text = "Profile Screen",
            modifier = Modifier.padding(it)
        )
    }
}