package com.example.aboutcanada.networking

import androidx.annotation.MainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response

/**
 * Class to get the successful and failure API response
 */
@ExperimentalCoroutinesApi
abstract class NetworkOnlineDataRepo<RESULT, REQUEST> {
    fun asFlow() = flow {
        emit(DataResult.Loading())
        try {
            val apiResponse = fetchDataFromRemoteSource()
            val data = apiResponse.body()
            // Success Response Data
            if (apiResponse.isSuccessful && data != null) {
                emit(DataResult.Success(data))
            } else {
                val errorData = getErrorMsg(apiResponse.errorBody()!!)
                emit(DataResult.Failure(errorData))
            }
        } catch (e: Exception) {
        }

    }

    /**
     * Error Response Data
     */
    private fun getErrorMsg(responseBody: ResponseBody): ArrayList<String> {

        val errorArrayList = ArrayList<String>()
        val errorMsg = responseBody.string()
        val jsonObject = JSONObject(errorMsg)

        val errors = jsonObject.getJSONArray("errors")

        for (i in 0 until errors.length()) {
            errorArrayList.add(errors[i] as String)
        }
        return errorArrayList
    }

    @MainThread
    protected abstract suspend fun fetchDataFromRemoteSource(): Response<REQUEST>
}