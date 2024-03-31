package com.yuvarajcode.food_harbor.presentation.main.news

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuvarajcode.food_harbor.domain.model.News
import com.yuvarajcode.food_harbor.domain.model.NewsAttributes
import com.yuvarajcode.food_harbor.domain.repository.NewsRepository
import com.yuvarajcode.food_harbor.utilities.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
):ViewModel(){
    private var _newsState = mutableStateOf<ResponseState<List<NewsAttributes>>>(ResponseState.Loading)
    val newsState : State<ResponseState<List<NewsAttributes>>> = _newsState
    init {
        getNewsDetails()
    }
    fun getNewsDetails() {
        viewModelScope.launch {
            try {
                val listOfNews = newsRepository.getNews()
                val realList = listOfNews.newsAttributes.filterNotNullAttributes()
                _newsState.value = ResponseState.Success(realList)
            } catch (e: Exception) {
                _newsState.value = ResponseState.Error(e.localizedMessage ?: "An unexpected error occurred")
            }
        }
    }
    private fun List<NewsAttributes>.filterNotNullAttributes(): List<NewsAttributes> {
        return this.filter { it.url != null && it.title != null && it.description != null && it.urlToImage != null}
    }
}