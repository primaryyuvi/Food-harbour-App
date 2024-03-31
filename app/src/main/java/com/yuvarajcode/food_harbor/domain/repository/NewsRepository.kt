package com.yuvarajcode.food_harbor.domain.repository

import com.yuvarajcode.food_harbor.domain.model.News
import com.yuvarajcode.food_harbor.utilities.ResponseState

interface NewsRepository {
    suspend fun getNews(): News
}