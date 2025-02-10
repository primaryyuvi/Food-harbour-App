package com.yuvarajcode.food_harbor.presentation.profile.main

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.yuvarajcode.food_harbor.domain.model.User
import com.yuvarajcode.food_harbor.presentation.authentication.AuthenticationViewModel
import com.yuvarajcode.food_harbor.presentation.main.BottomNavigation
import com.yuvarajcode.food_harbor.presentation.main.BottomNavigationScreens
import com.yuvarajcode.food_harbor.presentation.profile.ProfileViewmodel
import com.yuvarajcode.food_harbor.utilities.ResponseState
import com.yuvarajcode.food_harbor.utilities.Screens
import com.yuvarajcode.food_harbor.utilities.ToastForResponseState


@Composable
fun ProfileStateScreen(
    navController: NavController,
    profileViewmodel: ProfileViewmodel = hiltViewModel(),
    authenticationViewModel: AuthenticationViewModel = hiltViewModel()
) {
    when (val getData = profileViewmodel.getData.value) {
        is ResponseState.Success -> {
            val obj = getData.data
            Log.d("ProfileScreen", "ProfileScreen,ActualUser : $obj")
            if (obj != null) {
                profileViewmodel.realObj = obj
                ProfileScreen(
                    navController = navController,
                    profileViewmodel = profileViewmodel,
                    obj = obj,
                    authenticationViewModel = authenticationViewModel
                )
            } else {
                ToastForResponseState(message = "User Not Found")
                Log.d("ProfileStateScreen", "ProfileStateScreen,User Not Found")
            }
        }

        is ResponseState.Error -> {
            ToastForResponseState(message = getData.message)
            Log.d("ProfileStateScreen", "ProfileStateScreen,error: ${getData.message}")
        }

        is ResponseState.Loading -> {
            CircularProgressIndicator()
        }

        ResponseState.Initial -> {}
    }
}

@Composable
fun ProfileScreen(
    navController: NavController,
    profileViewmodel: ProfileViewmodel,
    obj: User,
    authenticationViewModel: AuthenticationViewModel
) {
    Scaffold(
        bottomBar = {
            BottomNavigation(
                selectedButton = BottomNavigationScreens.Profile,
                navController = navController
            )
        },
    ) {

        Column(
            modifier = Modifier.padding(it)
        ) {
//                ProfileViewCard(obj,navController,profileViewmodel)
//                Spacer(modifier = Modifier.height(30.dp))
//                DonationHistoryCard(navController)
                if (profileViewmodel.realObj.isUser) {
                    ProfileUserScreen(
                        authenticationViewModel = authenticationViewModel,
                        navController = navController,
                        profileViewmodel = profileViewmodel,
                    )
                } else {
                    ProfileOrgScreen(
                        authenticationViewModel = authenticationViewModel,
                        navController = navController,
                        profileViewmodel = profileViewmodel,
                    )
                }

        }


    }
}

@Composable
fun SignOutButton(
    authenticationViewModel: AuthenticationViewModel,
    navController: NavController
) {
    Button(
        onClick = {
            authenticationViewModel.chatLogout()
            authenticationViewModel.signOut()
        },
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(255, 229, 229),
            contentColor = Color(255, 68, 68)
        )
    ) {
        Text(
            text = "Sign Out",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        when (val signOut = authenticationViewModel.signOut.value) {
            is ResponseState.Success -> {
                when (signOut.data) {
                    true -> {
                        ToastForResponseState(message = "Sign Out Successfully")
                        navController.navigate(Screens.SplashScreen1.route) {
                            popUpTo(Screens.ProfileStateScreen.route) {
                                inclusive = true
                            }
                        }
                    }

                    false -> ToastForResponseState(message = "Sign Out Failed")
                    else -> {

                    }
                }
            }

            is ResponseState.Error -> {
                ToastForResponseState(message = signOut.message)
            }

            is ResponseState.Loading -> {
                CircularProgressIndicator()
            }

            ResponseState.Initial -> {}
        }
    }
}

@Composable
fun ProfileViewCard(
    obj: User,
    navController: NavController,
    profileViewmodel: ProfileViewmodel
) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .padding(top = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
            ) {

                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(obj.profilePictureUrl)
                        .build(),
                    contentDescription = "Profile Picture of the user or organisations ",
                    modifier = Modifier
                        .aspectRatio(1f, matchHeightConstraintsFirst = true)
                        .border(1.dp, Color.LightGray, shape = CircleShape)
                        .padding(30.dp)
                        .clip(CircleShape)
                        .size(70.dp)
                        .weight(3f)
                )
                Column(
                    modifier = Modifier
                        .weight(7f)
                        .padding(24.dp)
                ) {
                    Text(
                        text = " ${obj.userName}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(4.dp)
                    )
                    Text(
                        text = obj.email,
                        style = MaterialTheme.typography.titleSmall,
                    )
                }
            }
            OutlinedButton(
                onClick = {
                    navController.navigate(Screens.ProfileEditScreen.route) {
                        popUpTo(Screens.ProfileStateScreen.route) {
                            inclusive = false
                        }
                    }
                    Log.d("Navigation", "Navigating to ProfileEditScreen")
                    profileViewmodel.setUserState()
                },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Edit Profile",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTopBar() {
    TopAppBar(
        title = {
            Text(text = "Profile")
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
            titleContentColor = Color.White
        ),
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    )
}


// Define colors


//@Preview
//@Composable
//fun ProfileScreenPreview() {
//    ProfileScreen()
//}