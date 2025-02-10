package com.yuvarajcode.food_harbor.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yuvarajcode.food_harbor.presentation.authentication.AuthenticationViewModel
import com.yuvarajcode.food_harbor.presentation.authentication.auth.LoginScreen
import com.yuvarajcode.food_harbor.presentation.authentication.auth.RegisterScreen
import com.yuvarajcode.food_harbor.presentation.authentication.beforeauth.RegisterInfoScreen
import com.yuvarajcode.food_harbor.presentation.authentication.beforeauth.Screen1
import com.yuvarajcode.food_harbor.presentation.authentication.beforeauth.SplashScreen1
import com.yuvarajcode.food_harbor.presentation.main.donation.DonationScreen
import com.yuvarajcode.food_harbor.presentation.main.donation.DonationViewModel
import com.yuvarajcode.food_harbor.presentation.main.home.HomeScreen
import com.yuvarajcode.food_harbor.presentation.main.news.NewsScreen
import com.yuvarajcode.food_harbor.presentation.main.chat.ChatScreen
import com.yuvarajcode.food_harbor.presentation.main.chat.ChatViewModel
import com.yuvarajcode.food_harbor.presentation.main.chat.MessageScreen
import com.yuvarajcode.food_harbor.presentation.main.donation.form.DonateFormContactDetails
import com.yuvarajcode.food_harbor.presentation.main.donation.form.DonateFormPickup
import com.yuvarajcode.food_harbor.presentation.main.donation.form.DonateFormReview
import com.yuvarajcode.food_harbor.presentation.main.donation.form.DonationFormScreen
import com.yuvarajcode.food_harbor.presentation.main.donation.form.DonationFormViewModel
import com.yuvarajcode.food_harbor.presentation.main.donation.organization.DonationOrgViewModel
import com.yuvarajcode.food_harbor.presentation.main.donation.user.DonationUserScreenViewModel
import com.yuvarajcode.food_harbor.presentation.main.home.HomeScreenViewModel
import com.yuvarajcode.food_harbor.presentation.profile.donationHistory.DonationHistoryScreen
import com.yuvarajcode.food_harbor.presentation.profile.edit.ProfileEditScreen
import com.yuvarajcode.food_harbor.presentation.main.news.NewsViewModel
import com.yuvarajcode.food_harbor.presentation.profile.main.ProfileStateScreen
import com.yuvarajcode.food_harbor.presentation.profile.ProfileViewmodel
import com.yuvarajcode.food_harbor.presentation.profile.donationHistory.DonationHistoryViewModel
import com.yuvarajcode.food_harbor.utilities.Screens

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationHost(
    navController: NavHostController,
    authViewModel : AuthenticationViewModel,
    profileViewmodel : ProfileViewmodel,
    newsViewModel: NewsViewModel,
    donationViewModel: DonationViewModel,
    donationUserScreenViewModel: DonationUserScreenViewModel,
    donationFormViewModel: DonationFormViewModel,
    donationOrgViewModel: DonationOrgViewModel,
    homeScreenViewModel: HomeScreenViewModel,
    donationHistoryViewModel: DonationHistoryViewModel,
    chatViewModel: ChatViewModel
){
    NavHost(
        navController = navController,
        startDestination = Screens.SplashScreen1.route
    ) {
        composable(Screens.SplashScreen1.route){
            SplashScreen1(navController = navController, viewModel = authViewModel)
        }
        composable(Screens.Screen1.route){
            Screen1(navController = navController)
        }
        composable(Screens.RegisterInfoScreen.route){
            RegisterInfoScreen(navController = navController, authViewModel)
        }
        composable(Screens.RegisterScreen.route){
            RegisterScreen(navController = navController, authViewModel)
        }
        composable(Screens.LoginScreen.route){
            LoginScreen(navController = navController, authViewModel,chatViewModel)
        }
        composable(Screens.HomeScreen.route){
           HomeScreen(navController = navController,homeScreenViewModel)
        }
        composable(Screens.DonationScreen.route){
            val isUser = homeScreenViewModel.userData.isUser
            DonationScreen(
                navController = navController,
                donationUserScreenViewModel,
                donationOrgViewModel,
                isUser = isUser
            )
        }
        composable(Screens.ChatScreen.route){
            ChatScreen(
                navController,
                chatViewModel,
            )
        }
        composable(Screens.ProfileStateScreen.route){
            ProfileStateScreen(navController = navController,profileViewmodel = profileViewmodel, authenticationViewModel = authViewModel)
        }
        composable(Screens.NewsScreen.route){
            NewsScreen(navController = navController,newsViewModel)
        }
        composable(Screens.ProfileEditScreen.route){
            ProfileEditScreen(navController = navController,profileViewmodel = profileViewmodel)
        }
        composable(Screens.DonationHistoryScreen.route){
            val isUser = homeScreenViewModel.userData.isUser
            DonationHistoryScreen(
                navController,
                isUser,
                donationHistoryViewModel
            )
        }
        composable(Screens.DonationFormScreen.route){
            DonationFormScreen(navController = navController, donationFormViewModel)
//            DonationForm()
        }
        composable(Screens.MessageScreen.route){
            val channelId = chatViewModel.channelId
            MessageScreen(navController,channelId)
        }
        composable(Screens.DonationFormPickup.route){
            DonateFormPickup(donationFormViewModel,navController)
        }
        composable(Screens.DonationFormReview.route){
            DonateFormReview( navController,donationFormViewModel)
        }
        composable(Screens.DonationFormContactDetails.route){
            DonateFormContactDetails(navController, donationFormViewModel)
        }
    }
}


