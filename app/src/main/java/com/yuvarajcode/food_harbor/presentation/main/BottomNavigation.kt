package com.yuvarajcode.food_harbor.presentation.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.twotone.Menu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yuvarajcode.food_harbor.utilities.Screens

enum class BottomNavigationScreens(val route: String,val icon:ImageVector) {
    Home(Screens.HomeScreen.route,Icons.Filled.Home),
    News(Screens.NewsScreen.route,ImageVector.Builder(
        name = "newspaper",
        defaultWidth = 40.0.dp,
        defaultHeight = 40.0.dp,
        viewportWidth = 40.0f,
        viewportHeight = 40.0f
    ).apply {
        path(
            fill = SolidColor(Color.Black),
            fillAlpha = 1f,
            stroke = null,
            strokeAlpha = 1f,
            strokeLineWidth = 1.0f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Miter,
            strokeLineMiter = 1f,
            pathFillType = PathFillType.NonZero
        ) {
            moveTo(6.042f, 34.917f)
            quadToRelative(-1.084f, 0f, -1.854f, -0.771f)
            quadToRelative(-0.771f, -0.771f, -0.771f, -1.854f)
            verticalLineTo(6.542f)
            quadToRelative(0f, -0.584f, 0.271f, -0.709f)
            quadToRelative(0.27f, -0.125f, 0.687f, 0.292f)
            lineToRelative(1.75f, 1.75f)
            lineToRelative(1.833f, -1.833f)
            quadToRelative(0.209f, -0.209f, 0.438f, -0.292f)
            quadToRelative(0.229f, -0.083f, 0.479f, -0.083f)
            quadToRelative(0.25f, 0f, 0.479f, 0.062f)
            quadToRelative(0.229f, 0.063f, 0.438f, 0.271f)
            lineToRelative(1.875f, 1.875f)
            lineTo(13.542f, 6f)
            quadToRelative(0.208f, -0.208f, 0.437f, -0.271f)
            quadToRelative(0.229f, -0.062f, 0.479f, -0.062f)
            quadToRelative(0.25f, 0f, 0.48f, 0.083f)
            quadToRelative(0.229f, 0.083f, 0.437f, 0.292f)
            lineToRelative(1.833f, 1.833f)
            lineTo(19.083f, 6f)
            quadToRelative(0.209f, -0.208f, 0.438f, -0.292f)
            quadToRelative(0.229f, -0.083f, 0.479f, -0.083f)
            quadToRelative(0.25f, 0f, 0.479f, 0.083f)
            quadToRelative(0.229f, 0.084f, 0.438f, 0.292f)
            lineToRelative(1.875f, 1.875f)
            lineToRelative(1.833f, -1.833f)
            quadToRelative(0.208f, -0.209f, 0.437f, -0.292f)
            quadToRelative(0.23f, -0.083f, 0.48f, -0.083f)
            quadToRelative(0.25f, 0f, 0.479f, 0.062f)
            quadToRelative(0.229f, 0.063f, 0.437f, 0.271f)
            lineToRelative(1.875f, 1.875f)
            lineTo(30.208f, 6f)
            quadToRelative(0.209f, -0.208f, 0.438f, -0.271f)
            quadToRelative(0.229f, -0.062f, 0.479f, -0.062f)
            quadToRelative(0.25f, 0f, 0.479f, 0.083f)
            quadToRelative(0.229f, 0.083f, 0.438f, 0.292f)
            lineToRelative(1.833f, 1.833f)
            lineToRelative(1.75f, -1.75f)
            quadToRelative(0.417f, -0.417f, 0.708f, -0.292f)
            quadToRelative(0.292f, 0.125f, 0.292f, 0.709f)
            verticalLineToRelative(25.75f)
            quadToRelative(0f, 1.083f, -0.792f, 1.854f)
            quadToRelative(-0.791f, 0.771f, -1.875f, 0.771f)
            close()
            moveToRelative(0f, -2.625f)
            horizontalLineToRelative(12.666f)
            verticalLineToRelative(-11.25f)
            horizontalLineTo(6.042f)
            verticalLineToRelative(11.25f)
            close()
            moveToRelative(15.291f, 0f)
            horizontalLineToRelative(12.625f)
            verticalLineToRelative(-4.334f)
            horizontalLineTo(21.333f)
            close()
            moveToRelative(0f, -6.959f)
            horizontalLineToRelative(12.625f)
            verticalLineToRelative(-4.291f)
            horizontalLineTo(21.333f)
            close()
            moveTo(6.042f, 18.375f)
            horizontalLineToRelative(27.916f)
            verticalLineTo(13.25f)
            horizontalLineTo(6.042f)
            close()
        }
    }.build()),
    Donation(Screens.DonationScreen.route,Icons.Rounded.Favorite),
    Chat(Screens.ChatScreen.route, Icons.AutoMirrored.Filled.Chat),
    Profile(Screens.ProfileStateScreen.route,Icons.Filled.AccountCircle),
}
@Composable
fun BottomNavigation(
    selectedButton : BottomNavigationScreens,
    navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (item in BottomNavigationScreens.entries) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Image(
                    imageVector = item.icon,
                    contentDescription = item.name,
                    modifier = Modifier
                        .clickable {
                            navController.navigate(item.route)
                        }
                        .padding(8.dp)
                        .padding(top = 8.dp)
                        .size(24.dp)
                        .align(Alignment.CenterHorizontally),
                    colorFilter = if (selectedButton == item) {
                        ColorFilter.tint(Color(red = 7, green = 31, blue = 27, alpha = 255))
                    } else {
                        ColorFilter.tint(Color.Gray)
                    }
                )
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = if (selectedButton == item) {
                        Color(red = 7, green = 31, blue = 27, alpha = 255)
                    } else {
                        Color.Gray
                    },
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}