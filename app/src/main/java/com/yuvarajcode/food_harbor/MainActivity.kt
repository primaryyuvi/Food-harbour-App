package com.yuvarajcode.food_harbor

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.yuvarajcode.food_harbor.navigation.NavigationHost
import com.yuvarajcode.food_harbor.presentation.authentication.AuthenticationViewModel
import com.yuvarajcode.food_harbor.presentation.main.donation.form.DonationFormViewModel
import com.yuvarajcode.food_harbor.presentation.main.donation.user.DonationUserScreenViewModel
import com.yuvarajcode.food_harbor.presentation.main.donation.DonationViewModel
import com.yuvarajcode.food_harbor.presentation.main.donation.organization.DonationOrgViewModel
import com.yuvarajcode.food_harbor.presentation.profile.ProfileViewmodel
import com.yuvarajcode.food_harbor.presentation.main.news.NewsViewModel
import com.yuvarajcode.food_harbor.ui.theme.FoodHarborTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodHarborTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    val authViewModel : AuthenticationViewModel = hiltViewModel()
                    val newsViewModel : NewsViewModel = hiltViewModel()
                    val profileViewmodel : ProfileViewmodel = hiltViewModel()
                    val donationViewModel : DonationViewModel = hiltViewModel()
                    val donationUserScreenViewModel : DonationUserScreenViewModel = hiltViewModel()
                    val donationFormViewModel : DonationFormViewModel = hiltViewModel()
                    val donationOrgViewModel : DonationOrgViewModel = hiltViewModel()
                    NavigationHost(
                        navController = navController,
                        authViewModel = authViewModel,
                        profileViewmodel = profileViewmodel,
                        newsViewModel = newsViewModel,
                        donationViewModel = donationViewModel,
                        donationUserScreenViewModel = donationUserScreenViewModel,
                        donationFormViewModel = donationFormViewModel,
                        donationOrgViewModel = donationOrgViewModel
                    )     
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FoodHarborTheme {

    }
}
