package com.yuvarajcode.food_harbor.beforeauth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yuvarajcode.food_harbor.R

@Composable
fun Heading() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(4.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(100.dp)
                .clip(MaterialTheme.shapes.small)
        )
        Text(
            text = "Food Harbor",
            style = MaterialTheme.typography.displayMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(2.dp)
        )
    }
}

@Composable
fun Screen1() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(
            Color(red = 7, green = 31, blue = 27, alpha = 255),
        ),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Heading()
            Text(
                text = "Welcome!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 16.dp),
                color = Color.White
            )
            Text(
                text = "Sign up to donate food and help the needy!",
                color = Color.White,
            )
        }
            Spacer(modifier = Modifier.height(280.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White, RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AccessButtons()
            }
        }
    }
@Composable
fun AccessButtons() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextButton(
            onClick = {},
            modifier = Modifier
                .widthIn(min = 280.dp)
                .heightIn(min = 48.dp),
            colors = ButtonDefaults.textButtonColors(
                containerColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
                contentColor = Color.White
            ),

        ){
            Text(text = "Login")
        }
        TextButton(
            onClick = {},
            modifier = Modifier
                .widthIn(min = 280.dp)
                .heightIn(min = 48.dp),
            colors = ButtonDefaults.textButtonColors(
                containerColor =  Color(red = 7, green = 31, blue = 27, alpha = 255),
                contentColor =  Color.White
            ),
        ) {
            Text(text = "Register")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Screen1Preview()
{
    Screen1()
}