package com.yuvarajcode.food_harbor.presentation.main.news

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.yuvarajcode.food_harbor.R
import com.yuvarajcode.food_harbor.domain.model.NewsAttributes
import com.yuvarajcode.food_harbor.presentation.main.BottomNavigation
import com.yuvarajcode.food_harbor.presentation.main.BottomNavigationScreens
import com.yuvarajcode.food_harbor.utilities.ResponseState
import com.yuvarajcode.food_harbor.utilities.Screens

@Composable
fun NewsScreen(
    navController: NavController,
    newsViewModel: NewsViewModel
) {
    val newsState = newsViewModel.newsState.value
    Scaffold(
        topBar = {
              NewsTopBar()
        },
        bottomBar = {
            BottomNavigation(
                selectedButton = BottomNavigationScreens.News, navController = navController
            )
        }
    ) {
        Box (modifier = Modifier.padding(it)) {
            when(newsState){
                is ResponseState.Loading -> {
                    Text(text = "Loading")
                }
                is ResponseState.Success -> {
                    NewsDisplay(news = newsState.data)
                }
                is ResponseState.Error -> {
                    Text(text = newsState.message)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsTopBar() {
    CenterAlignedTopAppBar(
        title = { Text(text = "News") },
        colors = TopAppBarColors(
            containerColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
            scrolledContainerColor = Color(red = 7, green = 31, blue = 27, alpha = 255),
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White,
            titleContentColor = Color.White
        )
    )
}

@Composable
fun NewsDisplay(
    news: List<NewsAttributes>,
) {
    LazyColumn(
        modifier= Modifier
    ) {
        items(news) { newsItem ->
            NewsCard(
                newsItem = newsItem,
            )
        }
    }
}

@Composable
fun NewsCard(
    newsItem: NewsAttributes,
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
            pressedElevation = 4.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        )
    ) {
        val ctx = LocalContext.current
        val title = newsItem.title
        val description = newsItem.description
        val url = newsItem.url ?: ""
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(newsItem.urlToImage)
                    .crossfade(true)
                    .build(),
                contentDescription = "News images",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth(),
                error = painterResource(id = R.drawable.ic_broken_image),
            )
            Text(
                text = if(title != null) newsItem.title else "No Title",
                modifier = Modifier.padding(8.dp),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = if(description != null) newsItem.description else "No Description",
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            TextButton(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                   ctx.startActivity(intent)
                },
            ) {
                Text(text = "Read More")
            }
        }
    }
}