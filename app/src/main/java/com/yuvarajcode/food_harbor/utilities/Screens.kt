package com.yuvarajcode.food_harbor.utilities

sealed class Screens (val route:String){
    data object Screen1: Screens("screen1")
    data object SplashScreen1: Screens("SplashScreen1")
    data object LoginScreen: Screens("LoginScreen")
    data object RegisterScreen: Screens("RegisterScreen")
    data object RegisterInfoScreen: Screens("RegisterInfoScreen")
    data object HomeScreen: Screens("HomeScreen")
    data object ProfileStateScreen: Screens("ProfileStateScreen")
    data object ProfileEditScreen : Screens("ProfileEditScreen")
    data object DonationHistoryScreen: Screens("DonationHistoryScreen")
    data object NewsScreen: Screens("NewsScreen")
    data object ChatScreen: Screens("ChatScreen")
    data object DonationScreen: Screens("DonationScreen")
    data object DonationFormScreen : Screens("DonationFormScreen")
    data object MessageScreen: Screens("MessageScreen")
    data object DonationFormReview: Screens("DonationFormReview")
    data object DonationFormPickup: Screens("DonationFormPickup")
    data object DonationFormFoodDetails : Screens("DonationFormFoodDetails")
    data object DonationFormContactDetails : Screens("DonationFormReviewDetails")
}