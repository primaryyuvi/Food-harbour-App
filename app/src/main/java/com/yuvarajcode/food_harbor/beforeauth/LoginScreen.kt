package com.yuvarajcode.food_harbor.beforeauth

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(red = 7, green = 31, blue = 27, alpha = 255)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(150.dp))
        LoginHeading()
        LoginForm()
    }
}

@Composable
fun LoginForm() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .padding(36.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        LoginTextField("Email", "Password")
        LoginTextField("Password", "Password")
        Spacer(modifier = Modifier.height(24.dp))
        LoginButton()
    }
}

@Composable
fun LoginButton() {
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
            text = "Login",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}

@Composable
fun LoginTextField(
    label : String,
    placeholder : String
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

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen()
}

