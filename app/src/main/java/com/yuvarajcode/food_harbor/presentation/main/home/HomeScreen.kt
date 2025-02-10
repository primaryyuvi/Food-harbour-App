package com.yuvarajcode.food_harbor.presentation.main.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.yuvarajcode.food_harbor.R
import com.yuvarajcode.food_harbor.domain.model.Donation
import com.yuvarajcode.food_harbor.domain.model.DonationStatus
import com.yuvarajcode.food_harbor.domain.model.User
import com.yuvarajcode.food_harbor.presentation.main.BottomNavigation
import com.yuvarajcode.food_harbor.presentation.main.BottomNavigationScreens
import com.yuvarajcode.food_harbor.utilities.ResponseState
import com.yuvarajcode.food_harbor.utilities.ToastForResponseState


@Composable
fun HomeScreen(
    navController: NavController,
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel()
) {
    Scaffold(
        bottomBar = {
            BottomNavigation(
                selectedButton = BottomNavigationScreens.Home, navController = navController
            )
        },
    ) {

        when(val response = homeScreenViewModel.getUserData.value){
            is ResponseState.Success -> {
                homeScreenViewModel.userData = response.data!!
            }
            is ResponseState.Error -> {
                ToastForResponseState(response.message)
            }
            is ResponseState.Loading -> {

            }

            ResponseState.Initial -> {

            }
        }
        Column(
            modifier = Modifier.padding(it)
        ) {
            if (homeScreenViewModel.userData.isUser) {
                HomeUserScreen(
                    navController = navController,
                    homeScreenViewModel = homeScreenViewModel
                )
            } else {
                HomeOrgScreen(
                    navController = navController,
                    homeScreenViewModel = homeScreenViewModel
                )
            }
        }
    }


}

//
//@Composable
//fun HomeShowScreen(
//    navController: NavController,
//    donationList: List<Donation>,
//    homeScreenViewModel: HomeScreenViewModel
//) {
//    Scaffold (
//        bottomBar = {
//            BottomNavigation(
//                selectedButton = BottomNavigationScreens.Home, navController = navController
//             )
//        },
//        topBar = {
//            HomeScreenTopBar()
//        }
//    ){
//        Box (
//            modifier = Modifier
//                .padding(it)
//                .fillMaxSize(),
//        ){
//            if(donationList.isEmpty()){
//                Text(
//                    text = "No Donations available",
//                    style = MaterialTheme.typography.headlineMedium,
//                    modifier = Modifier.align(Alignment.Center)
//                )
//            } else {
//                LazyColumn {
//                    items(donationList.size) { index ->
//                        DonationCard(
//                            donation = donationList[index],
//                            homeScreenViewModel = homeScreenViewModel
//                        )
//                    }
//                }
//            }
//        }
//    }
//}






//@Composable
//@Preview
//fun FoodShareScreenPreview() {
//    HomeUserScreen()
//}

