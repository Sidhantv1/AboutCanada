package com.example.aboutcanada.networking

/**
 * Data Results Types
 */
sealed class DataResult<out T> {
    // Loader set when the data is being fetched
    data class Loading(val nothing: Nothing? = null) : DataResult<Nothing>()

    // Data fetched successfully from API
    data class Success<out T>(val data: T) : DataResult<T>()

    // In case of some failure while fetching the data from the API Response
    data class Failure(val message: ArrayList<String>, val exception: Exception? = null) :
        DataResult<Nothing>()

}