package com.example.aboutcanada.networking


sealed class DataResult<out T> {

    data class Loading(val nothing: Nothing? = null) : DataResult<Nothing>()

    data class Success<out T>(val data: T) : DataResult<T>()

    data class Failure(val message: ArrayList<String>, val exception: Exception? = null) :
        DataResult<Nothing>()

}