package com.yuvarajcode.food_harbor.domain.repository

import com.yuvarajcode.food_harbor.domain.model.TokenClass
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface StreamApi {
    @GET("token")
    suspend fun  getToken(
        @Query("user_id") userId: String
    ): TokenClass

    companion object {
        const val BASE_URL = "https://food-harbor-backend.vercel.app/api/"
        operator fun invoke(): StreamApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(StreamApi::class.java)
        }
    }
}