package com.yuvarajcode.food_harbor.presentation.authentication.beforeauth

import android.util.Log
import android.view.animation.OvershootInterpolator
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.yuvarajcode.food_harbor.R
import com.yuvarajcode.food_harbor.utilities.Screens
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yuvarajcode.food_harbor.presentation.authentication.AuthenticationViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen1(
    navController: NavController,
    viewModel: AuthenticationViewModel
) {
    val authValue = viewModel.isUserAuthenticated
    Log.d("SplashScreen1", "SplashScreen1: $authValue")
    val scale = remember {
            Animatable(0f)
    }
    LaunchedEffect(key1 =true) {
        scale.animateTo(
            targetValue = 0.5f,
            animationSpec = tween(
                durationMillis = 1500,
                easing = {
                    OvershootInterpolator(2f).getInterpolation(it)
                }
            )
        )
        delay(1000)
        if (authValue) {
            navController.navigate(Screens.HomeScreen.route){
                popUpTo(Screens.SplashScreen1.route){
                    inclusive = true
                }
            }
        } else {
            navController.navigate(Screens.Screen1.route){
                popUpTo(Screens.SplashScreen1.route){
                    inclusive = true
                }
            }
        }
    }

    Box (
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
            .background(Color(red = 7, green = 31, blue = 27, alpha = 255))
    ){
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Splash screen picture",
            modifier = Modifier.scale(scale.value)
                .size(130.dp)
        )
    }
}

@Preview
@Composable
fun SplashScreen1Preview() {
    Box (
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
            .background(Color(red = 7, green = 31, blue = 27, alpha = 255))
    ){
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Splash screen picture",
            modifier = Modifier
                .size(130.dp)
        )
    }
}