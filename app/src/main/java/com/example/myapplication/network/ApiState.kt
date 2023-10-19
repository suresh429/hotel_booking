package com.example.myapplication.network

sealed class ApiState<out T> {
    data class Success<out R>(val data: R): ApiState<R>()
    data class Failure(val message: String): ApiState<Nothing>()
    data object Loading: ApiState<Nothing>()

}