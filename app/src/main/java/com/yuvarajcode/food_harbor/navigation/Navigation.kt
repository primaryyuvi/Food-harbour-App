package com.yuvarajcode.food_harbor.navigation

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
import com.yuvarajcode.food_harbor.presentation.main.DonationScreen
import com.yuvarajcode.food_harbor.presentation.main.HomeScreen
import com.yuvarajcode.food_harbor.presentation.main.news.NewsScreen
import com.yuvarajcode.food_harbor.presentation.main.OrganisationScreen
import com.yuvarajcode.food_harbor.presentation.main.news.NewsViewModel
import com.yuvarajcode.food_harbor.presentation.profile.ProfileScreen
import com.yuvarajcode.food_harbor.utilities.Screens

@Composable
fun NavigationHost(
    navController: NavHostController,
    authViewModel : AuthenticationViewModel,
    newsViewModel: NewsViewModel
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
            LoginScreen(navController = navController, authViewModel)
        }
        composable(Screens.HomeScreen.route){
           HomeScreen(navController = navController)
        }
        composable(Screens.DonationScreen.route){
            DonationScreen(navController = navController)
        }
        composable(Screens.OrganisationScreen.route){
            OrganisationScreen(navController = navController)
        }
        composable(Screens.ProfileScreen.route){
            ProfileScreen(navController = navController)
        }
        composable(Screens.NewsScreen.route){
            NewsScreen(navController = navController,newsViewModel)
        }
    }
}


