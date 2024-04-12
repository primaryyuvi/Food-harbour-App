package com.yuvarajcode.food_harbor.presentation.main.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Constraints
import androidx.navigation.NavController
import com.yuvarajcode.food_harbor.presentation.main.BottomNavigation
import com.yuvarajcode.food_harbor.presentation.main.BottomNavigationScreens

@Composable
fun ChatScreen(
    navController: NavController
) {
    Scaffold(
        bottomBar = {
             BottomNavigation(
                 selectedButton = BottomNavigationScreens.Chat,
                 navController = navController
             )
        }
    ) {
        Box (
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(it)
                .fillMaxSize()
        ){
            Text(
                text ="Chat Screen",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}