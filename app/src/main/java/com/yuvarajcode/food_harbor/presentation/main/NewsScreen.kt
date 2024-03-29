package com.yuvarajcode.food_harbor.presentation.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun NewsScreen(
    navController: NavController
) {
    Scaffold(
        bottomBar = {
            BottomNavigation(
                selectedButton = BottomNavigationScreens.News, navController = navController
            )
        }
    ) {

    Text(
        text ="News Screen",
        modifier = Modifier.padding(it)
    )
    }
}