package com.yuvarajcode.food_harbor.presentation.profile

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.yuvarajcode.food_harbor.R
import com.yuvarajcode.food_harbor.domain.model.User
import com.yuvarajcode.food_harbor.presentation.main.BottomNavigation
import com.yuvarajcode.food_harbor.presentation.main.BottomNavigationScreens
import com.yuvarajcode.food_harbor.utilities.ResponseState
import com.yuvarajcode.food_harbor.utilities.Screens
import com.yuvarajcode.food_harbor.utilities.ToastForResponseState


@Composable
fun ProfileStateScreen(
    navController : NavController,
    profileViewmodel: ProfileViewmodel
){
    when(val getData = profileViewmodel.getData.value){
        is ResponseState.Success -> {
            val obj = getData.data
            if (obj != null) {
                profileViewmodel.realObj = obj
                ProfileScreen(
                    navController = navController,
                    profileViewmodel = profileViewmodel,
                    obj = obj
                )
            }
        }
        is ResponseState.Error -> {
            ToastForResponseState(message =getData.message)
        }
        is ResponseState.Loading -> {
            CircularProgressIndicator()
        }
    }
}
@Composable
fun ProfileScreen(
    navController: NavController,
    profileViewmodel: ProfileViewmodel,
    obj : User
) {
    Scaffold(
        topBar = {
            ProfileTopBar()
        },
        bottomBar = {
            BottomNavigation(
                selectedButton = BottomNavigationScreens.Profile,
                navController = navController
            )
        },
    ) {
        Box (
            modifier = Modifier.padding(it)
        ){
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                ProfileViewCard(obj,navController,profileViewmodel)
                Spacer(modifier = Modifier.height(30.dp))
                DonationHistoryCard(navController)
            }
        }
    }
}

@Composable
fun DonationHistoryCard(
    navController: NavController
) {
        Card(
            onClick = {
                navController.navigate(Screens.DonationHistoryScreen.route){
                    popUpTo(Screens.ProfileStateScreen.route){
                        inclusive = true
                    }
                }
            },
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
            ){
                Icon(
                    imageVector = Icons.Rounded.Menu,
                    contentDescription = "Donation History",
                    modifier = Modifier.padding(8.dp)
                )
                Text(
                    text = "Donation History",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(8.dp),
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Arrow to navigate to the donation history",
                )
            }
        }
}

@Composable
fun ProfileViewCard(
    obj : User,
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
                    model= ImageRequest.Builder(context = LocalContext.current)
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
                    navController.navigate(Screens.ProfileEditScreen.route){
                        popUpTo(Screens.ProfileStateScreen.route){
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
