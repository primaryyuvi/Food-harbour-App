package com.yuvarajcode.food_harbor.presentation.main.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.yuvarajcode.food_harbor.domain.model.User
import com.yuvarajcode.food_harbor.presentation.main.BottomNavigation
import com.yuvarajcode.food_harbor.presentation.main.BottomNavigationScreens
import com.yuvarajcode.food_harbor.utilities.Screens
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme

@Composable
fun ChatScreen(
    navController: NavController,
    chatViewModel: ChatViewModel,
) {
    Scaffold(
        bottomBar = {
             BottomNavigation(
                 selectedButton = BottomNavigationScreens.Chat,
                 navController = navController
             )
        },
        topBar = {
            ChatTopBar()
        }
    ) {
        Box (
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ){
           ChatTheme {
                ChannelsScreen(
                    filters = Filters.`in`(
                        "members",
                        values = listOf(chatViewModel.userId ?: "")
                    ),
                    title = "Chat",
                    isShowingHeader = false,
                    isShowingSearch = false,
                    onItemClick = {channel ->
                        chatViewModel.channelId = channel.cid
                        navController.navigate(Screens.MessageScreen.route)
                    },
                )
           }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar() {
    CenterAlignedTopAppBar(
        title = { Text(
            text = "Chat",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        ) },
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surface,
            navigationIconContentColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
            actionIconContentColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
            titleContentColor = Color(red = 7, green = 31, blue = 27, alpha = 255)
        )
    )
}
