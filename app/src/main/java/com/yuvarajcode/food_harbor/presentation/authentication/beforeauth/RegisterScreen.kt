package com.yuvarajcode.food_harbor.presentation.authentication.beforeauth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RegisterScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(red = 7, green = 31, blue = 27, alpha = 255)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        RegisterHeading()
        RegisterForm(isUser = true)
        }
    }

@Composable
fun RegisterAccountButton() {
        TextButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.textButtonColors(
                containerColor = Color(red = 7, green = 31, blue = 27, alpha = 255)
            ),

        ) {
            Text(
                text = "Register",
                color = Color.White,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
        }
    }


@Composable
fun RegisterForm(
    isUser : Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .padding(36.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        if(isUser) {
            RegisterTextField(
                label = "Name",
                placeholder = "Enter your name"
            )
        }
        else{
            RegisterTextField(
                label = "Company Name",
                placeholder = "Enter your Company Name"
            )
        }
        RegisterTextField(label = "User Name", placeholder ="Enter your Username" )
        RegisterTextField(label = "Phone Number", placeholder = "Enter your phone number")
        RegisterTextField(label = "Email", placeholder = "Enter your email")
        RegisterTextField(label = "Password", placeholder = "Enter your password")

        Spacer(modifier = Modifier.height(16.dp))
        RegisterAccountButton()
    }
}

@Composable
fun RegisterTextField(
    label: String,
    placeholder: String
) {
    OutlinedTextField(
        value = "",
        onValueChange = {},
        label = { Text(label) },
        placeholder = { Text(placeholder)},
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
            unfocusedBorderColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
            unfocusedLabelColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
            focusedLabelColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun RegisterHeading() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Register",
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(8.dp),
            color = Color.White,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
        )
        Text(
            text ="Create an account to get started.",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(8.dp),
            color = Color.White
        )
    }

}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen()
}