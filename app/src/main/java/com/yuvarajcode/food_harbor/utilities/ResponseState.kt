package com.yuvarajcode.food_harbor.utilities

sealed class ResponseState <out T> {
    data class Success<T>(val data: T) : ResponseState<T>()
    data class Error<T>(val message: String) : ResponseState<T>()
    data object Loading : ResponseState<Nothing>()
}