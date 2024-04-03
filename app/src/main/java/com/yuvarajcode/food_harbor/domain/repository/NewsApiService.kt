package com.yuvarajcode.food_harbor.domain.repository

import com.yuvarajcode.food_harbor.domain.model.News
import com.yuvarajcode.food_harbor.utilities.ResponseState
import retrofit2.http.GET


interface NewsApiService {
    @GET("everything?q=\"waste-management\"&apiKey=db20ce91c53541ec87f8e282274bad7a")
    suspend fun getNews(): News
}