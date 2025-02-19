package com.yuvarajcode.food_harbor.presentation.authentication.auth

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yuvarajcode.food_harbor.domain.repository.StreamApi
import com.yuvarajcode.food_harbor.presentation.authentication.AuthenticationViewModel
import com.yuvarajcode.food_harbor.presentation.main.chat.ChatViewModel
import com.yuvarajcode.food_harbor.presentation.main.chat.StreamTokenProvider
import com.yuvarajcode.food_harbor.utilities.ResponseState
import com.yuvarajcode.food_harbor.utilities.Screens
import com.yuvarajcode.food_harbor.utilities.ToastForResponseState
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthenticationViewModel = hiltViewModel(),
    chatViewModel: ChatViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(red = 7, green = 31, blue = 27, alpha = 255))
            .wrapContentHeight()
            ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(150.dp))
        LoginHeading()
        LoginForm(
            navController = navController,
            authViewModel = authViewModel,
            chatViewModel = chatViewModel
        )
    }
}

@Composable
fun LoginForm(
    navController: NavController,
    authViewModel: AuthenticationViewModel,
    chatViewModel: ChatViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .padding(top = 16.dp)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        val email = remember{
            mutableStateOf("")
        }
        val password = remember{
            mutableStateOf("")
        }
        var showPassword by remember {
            mutableStateOf(false)
        }
        OutlinedTextField(
            value = email.value,
            onValueChange = {
                email.value = it
            },
            label = { Text("Enter your Email") },
            placeholder = { Text("Email")},
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
                unfocusedBorderColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
                unfocusedLabelColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
                focusedLabelColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
                focusedTextColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
                unfocusedTextColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
            ),
            modifier = Modifier,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        )
        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it},
            label = { Text("Enter your password") },
            placeholder = { Text("Password")},
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
                unfocusedBorderColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
                unfocusedLabelColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
                focusedLabelColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
                focusedTextColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
                unfocusedTextColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
            ),
            modifier = Modifier,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if(showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                if (password.value.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            showPassword = !showPassword
                        }
                    )
                    {
                        if(showPassword)
                            Icon(imageVector = Icons.Filled.Visibility,
                                contentDescription = "Password Visibility",
                                tint = Color(red = 7, green = 31, blue = 27, alpha = 255)
                            )
                        else
                            Icon(imageVector = Icons.Filled.VisibilityOff,
                                contentDescription = "Password Visibility",
                                tint = Color(red = 7, green = 31, blue = 27, alpha = 255)
                            )
                    }
                }
            }
        )
        Spacer(modifier = Modifier.height(24.dp))
        LoginButton(
            navController = navController,
            authViewModel = authViewModel,
            email = email.value,
            password = password.value
        )
    }
}

@Composable
fun LoginButton(
    navController: NavController,
    authViewModel: AuthenticationViewModel,
    email: String,
    password: String
) {
    val coroutineScope = rememberCoroutineScope()
    TextButton(
        onClick = {
            coroutineScope.launch {
                authViewModel.signIn(email, password)
            }
        },
        modifier = Modifier
            .height(48.dp)
            .width(250.dp),
        colors = ButtonDefaults.textButtonColors(
            containerColor = Color(red = 7, green = 31, blue = 27, alpha = 255)
        ),
    ) {
        Text(
            text = "Login",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        when(val response = authViewModel.signIn.value) {
            is ResponseState.Loading -> {
                CircularProgressIndicator()
            }

            is ResponseState.Success -> {
                val result = response.data
                when (result) {
                    true -> {
                        authViewModel.streamLogin()
                    }

                    false -> {
                        ToastForResponseState(message = "An unexpected error occurred")
                    }

                    null -> {

                    }
                }
            }

            is ResponseState.Error -> {
                ToastForResponseState(message = response.message)
            }

            ResponseState.Initial -> {}
        }
        when(val response = authViewModel.chatLogin.value) {
            is ResponseState.Success -> {
                val result = response.data
                Log.d("ChatViewModel", "loginGuest: $result")
                when (result) {
                    true -> {
                        navController.navigate(Screens.HomeScreen.route){
                            popUpTo(Screens.LoginScreen.route){
                                inclusive = true
                            }
                        }
                    }

                    false -> {
                        ToastForResponseState(message = "An unexpected error occurred in stream chat login")
                    }

                    null -> {

                    }
                }

            }
            is ResponseState.Error -> {
                ToastForResponseState(message = response.message)
            }

            ResponseState.Loading -> {
                CircularProgressIndicator()
            }

            ResponseState.Initial -> {}
        }
    }
}

@Composable
fun LoginHeading() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Login",
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(8.dp),
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Text(
            text ="Let's help the needy together!",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(8.dp),
            color = Color.White
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewLoginScreen() {
//    LoginScreen()
//}

