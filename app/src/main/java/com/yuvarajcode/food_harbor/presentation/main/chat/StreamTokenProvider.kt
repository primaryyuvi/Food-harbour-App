package com.yuvarajcode.food_harbor.presentation.main.chat

import com.yuvarajcode.food_harbor.domain.repository.StreamApi
import io.getstream.chat.android.client.token.TokenProvider
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class StreamTokenProvider (
    private val api : StreamApi
) {
    fun getToken(user_id :String) :TokenProvider {
        return object : TokenProvider {
            override fun loadToken(): String = runBlocking{
                 api.getToken(user_id).token
            }
        }
    }
}