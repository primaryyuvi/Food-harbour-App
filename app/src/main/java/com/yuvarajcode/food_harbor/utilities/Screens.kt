package com.yuvarajcode.food_harbor.utilities

sealed class Screens (val route:String){
    data object Screen1: Screens("screen1")
    data object SplashScreen1: Screens("SplashScreen1")
    data object LoginScreen: Screens("LoginScreen")
    data object RegisterScreen: Screens("RegisterScreen")
    data object RegisterInfoScreen: Screens("RegisterInfoScreen")
    data object HomeScreen: Screens("HomeScreen")
    data object ProfileScreen: Screens("ProfileScreen")
    data object NewsScreen: Screens("NewsScreen")
    data object OrganisationScreen: Screens("OrganisationScreen")
    data object DonationScreen: Screens("DonationScreen")
}