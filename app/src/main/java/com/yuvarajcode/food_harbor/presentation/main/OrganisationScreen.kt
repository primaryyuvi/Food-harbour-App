package com.yuvarajcode.food_harbor.presentation.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun OrganisationScreen(
    navController: NavController
){
    Scaffold(
        bottomBar = {
            BottomNavigation(
                selectedButton = BottomNavigationScreens.Organisation, navController = navController
            )
        }
    ) {
        Text(
            text = "Organisation Screen",
            modifier = Modifier.padding(it)
        )
    }
}