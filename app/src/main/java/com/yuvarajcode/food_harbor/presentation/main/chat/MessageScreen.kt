package com.yuvarajcode.food_harbor.presentation.main.chat

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import io.getstream.chat.android.compose.ui.messages.MessagesScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme

@Composable
fun MessageScreen(
    navController: NavController,
    channelId : String
) {
    ChatTheme {
        MessagesScreen(
            channelId =channelId,
            onBackPressed = {
                navController.popBackStack()
            },
            showHeader = true
        )
    }
}