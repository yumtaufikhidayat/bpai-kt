package com.taufik.ceritaku.data.remote

sealed class Result<out R> private constructor() {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val error: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
    data class Unauthorized(val error: String) : Result<Nothing>()
    data class ServerError(val error: String) : Result<Nothing>()
}