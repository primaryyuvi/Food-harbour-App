package com.yuvarajcode.food_harbor.data

import com.yuvarajcode.food_harbor.domain.model.News
import com.yuvarajcode.food_harbor.domain.repository.NewsApiService
import com.yuvarajcode.food_harbor.domain.repository.NewsRepository
import com.yuvarajcode.food_harbor.utilities.ResponseState
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApi: NewsApiService
) : NewsRepository {
    override suspend fun getNews(): News {
        return newsApi.getNews()
    }
}