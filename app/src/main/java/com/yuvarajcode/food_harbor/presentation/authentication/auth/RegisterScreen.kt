package com.yuvarajcode.food_harbor.presentation.authentication.auth

import android.annotation.SuppressLint
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
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yuvarajcode.food_harbor.presentation.authentication.AuthenticationViewModel
import com.yuvarajcode.food_harbor.utilities.ResponseState
import com.yuvarajcode.food_harbor.utilities.Screens
import com.yuvarajcode.food_harbor.utilities.ToastForResponseState

@Composable
fun RegisterScreen(
    navController: NavController,
    authViewModel: AuthenticationViewModel = hiltViewModel()
) {
    Log.d("RegisterScreen", "RegisterScreen,isUser: ${authViewModel.isUserState}")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(red = 7, green = 31, blue = 27, alpha = 255))
            .wrapContentHeight()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        RegisterHeading()
        RegisterForm(
            isUser = authViewModel.isUserState,
            navController = navController,
            authViewModel = authViewModel
        )
        }
    }

@Composable
fun RegisterHeading() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Register",
            color = Color.White,
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(8.dp),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Create an account to get started!",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(8.dp),
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun RegisterAccountButton(
    navController: NavController,
    authViewModel: AuthenticationViewModel,
    email: String,
    password: String,
    name: String,
    userName: String,
    phoneNumber: String,
) {
        TextButton(
            onClick = {
                Log.d("RegisterScreen", "RegisterAccountButton,isUser: ${authViewModel.isUserState}")
                authViewModel.register(name, userName, phoneNumber, email, password)
            },
            modifier = Modifier
                .width(250.dp)
                .height(48.dp),
            colors = ButtonDefaults.textButtonColors(
                containerColor = Color(red = 7, green = 31, blue = 27, alpha = 255)
            ),

        ) {
            Text(
                text = "Register",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            when(val response = authViewModel.register.value) {
                is ResponseState.Loading -> {
                    CircularProgressIndicator()
                }

                is ResponseState.Success -> {
                       ToastForResponseState(message ="Registration successful!")
                        if (response.data == true) {
                            authViewModel.streamLogin()
                        }
                    else if (response.data == false){
                        ToastForResponseState(message = "Registration failed to store in firebase!!!")
                    }
                    else
                    {

                    }
                }
                is ResponseState.Error -> {
                    ToastForResponseState(message = response.message)
                }

                ResponseState.Initial -> {}
            }
            when(val response = authViewModel.chatLogin.value)
            {
                is ResponseState.Error ->
                {
                    ToastForResponseState(message = "Chat login failed!")
                }
                ResponseState.Loading ->
                    CircularProgressIndicator()
                is ResponseState.Success -> {
                    val result = response.data
                    if (result == true) {
                        navController.navigate(Screens.HomeScreen.route) {
                            popUpTo(Screens.RegisterScreen.route) {
                                inclusive = true
                            }
                        }
                    } else if (result == false)
                        ToastForResponseState(message = "Chat Login Fail!!")
                    else if (result == null) {

                    }
                }

                ResponseState.Initial -> {}
            }
        }
}



@SuppressLint("SuspiciousIndentation")
@Composable
fun RegisterForm(
    isUser : Boolean,
    navController: NavController,
    authViewModel: AuthenticationViewModel
) {
    val email = remember {
        mutableStateOf("")
    }
    val password = remember {
        mutableStateOf("")
    }
    val name = remember {
        mutableStateOf("")
    }
    val phoneNumber = remember {
        mutableStateOf("")
    }
    val userName = remember {
        mutableStateOf("")
    }
    var showPassword by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .padding(36.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        if(isUser) {
            OutlinedTextField(
                value = name.value,
                onValueChange = { name.value = it },
                label = { Text("Enter your name") },
                placeholder = { Text("Name")},
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
                    unfocusedBorderColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
                    unfocusedLabelColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
                    focusedLabelColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
                    focusedTextColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
                    unfocusedTextColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
                ),
                modifier = Modifier
            )
        }
        else{
            OutlinedTextField(
                value = name.value,
                onValueChange = {name.value = it},
                label = { Text("Enter your company name") },
                placeholder = { Text("Company Name")},
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
                    unfocusedBorderColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
                    unfocusedLabelColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
                    focusedLabelColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
                    focusedTextColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
                    unfocusedTextColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
                ),
                modifier = Modifier
            )
        }
        OutlinedTextField(
            value = userName.value,
            onValueChange = {userName.value = it},
            label = { Text("Enter your username") },
            placeholder = { Text("Username")},
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
                unfocusedBorderColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
                unfocusedLabelColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
                focusedLabelColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
                focusedTextColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
                unfocusedTextColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
            ),
            modifier = Modifier
        )
        OutlinedTextField(
            value = phoneNumber.value,
            onValueChange = {phoneNumber.value = it},
            label = { Text("Enter your phone number") },
            placeholder = { Text("Phone Number")},
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
                unfocusedBorderColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
                unfocusedLabelColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
                focusedLabelColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
                focusedTextColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
                unfocusedTextColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
            ),
            modifier = Modifier,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = email.value,
            onValueChange = {email.value = it},
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        OutlinedTextField(
            value = password.value,
            onValueChange = {password.value = it},
            label = { Text("Enter your Password") },
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

        Spacer(modifier = Modifier.height(16.dp))
        RegisterAccountButton(
            navController = navController,
            authViewModel = authViewModel,
            email = email.value,
            password = password.value,
            name = name.value,
            userName = userName.value,
            phoneNumber = phoneNumber.value,
        )
    }
}



@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterHeading()
}