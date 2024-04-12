package com.yuvarajcode.food_harbor.presentation.authentication.beforeauth

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yuvarajcode.food_harbor.presentation.authentication.AuthenticationViewModel
import com.yuvarajcode.food_harbor.utilities.Screens

@Composable
fun RegisterInfoScreen(
    navController: NavController,
    authViewModel : AuthenticationViewModel
){
    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(red = 7, green = 31, blue = 27, alpha = 255))
    ){
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(130.dp))
            RegisterInfoHeading()
            RegisterInfoButtons(
                navController = navController,
                authViewModel = authViewModel
            )
        }
    }
}

@Composable
fun RegisterInfoButtons(
    navController: NavController,
    authViewModel: AuthenticationViewModel
) {
    val isClickedForIndi = remember {
        mutableStateOf(false)
    }
    val isClickedForOrg = remember {
        mutableStateOf(false)
    }
    val isUser = remember {
        mutableStateOf(false)
    }
    Column (
        modifier = Modifier
            .background(Color.White, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ){
        Card(
            onClick = {
                isClickedForIndi.value = true
                isClickedForOrg.value = false
                Log.d("RegisterInfoScreen", "RegisterInfoButtons: ${authViewModel.isUserState}")
            },
            modifier = Modifier.padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if(!isClickedForIndi.value)Color(red = 7, green = 31, blue = 27, alpha = 255)else Color.White,
                contentColor = if(!isClickedForIndi.value)Color.White else Color(red = 7, green = 31, blue = 27, alpha = 255)
            ),
            border = BorderStroke(1.dp, if(!isClickedForIndi.value)Color.White else Color(red = 7, green = 31, blue = 27, alpha = 255)),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp,
            )
        ) {
            Row {
                Column (
                    modifier = Modifier.padding(8.dp)
                ){
                    Text(
                        text = "Individual",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .padding(16.dp)
                            .padding(top = 0.dp),
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "You are a person who wants to donate food to various organisations",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
        Card(
            onClick = {
                isClickedForIndi.value = false
                isClickedForOrg.value = true
                Log.d("RegisterInfoScreen", "RegisterInfoButtons: ${authViewModel.isUserState}")
            },
            modifier = Modifier.padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if(!isClickedForOrg.value)Color(red = 7, green = 31, blue = 27, alpha = 255)else Color.White,
                contentColor = if(!isClickedForOrg.value)Color.White else Color(red = 7, green = 31, blue = 27, alpha = 255)
            ),
            border = BorderStroke(1.dp, if(!isClickedForOrg.value)Color.White else Color(red = 7, green = 31, blue = 27, alpha = 255)),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp,
            )
        ) {
            Row {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = "Organisation",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(16.dp),
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "You are an organisation that wants to receive food from various individuals",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
        Button(
            onClick = {
                isUser.value = isClickedForIndi.value
                Log.d("RegisterInfoScreen", "RegisterInfoButtons: ${isUser.value}")
                authViewModel.isUserOrNot(isUser.value)
                navController.navigate(Screens.RegisterScreen.route){
                    launchSingleTop = true
                }
            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.textButtonColors(
                contentColor = Color.White,
                containerColor = Color(red = 7, green = 31, blue = 27, alpha = 255)
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Save",
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun RegisterInfoHeading() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "What kind of user are you?",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp),
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text ="Select the type of user you are!",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(8.dp),
            color = Color.White,
            fontWeight = FontWeight.SemiBold
        )
    }
}


//@Preview(showBackground = true)
//@Composable
//fun RegisterInfoScreenPreview(){
//    RegisterInfoScreen()
//}

