package com.example.aboutcanada.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aboutcanada.repository.MainRepository
import com.example.aboutcanada.dataclass.FactsDataClassResponseModel
import com.example.aboutcanada.networking.DataResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel class
 */
class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {

    // private live data object for the API Response for viewModel
    private var _factsDataClassLiveDataResponseModel =
        MutableLiveData<DataResult<FactsDataClassResponseModel>>()

    // live data object for the API Response for Activity
    var factsDataClassLiveDataResponseModel: LiveData<DataResult<FactsDataClassResponseModel>> =
        _factsDataClassLiveDataResponseModel

    /**
     * Methods to call the API and fetch the data to be observed on the Activity
     */
    fun getFactsData(isPulledToRefresh: Boolean = false): LiveData<DataResult<FactsDataClassResponseModel>> {

        // Doesn't load the API again on orientation change when the livedata is not null
        if (_factsDataClassLiveDataResponseModel.value != null && !isPulledToRefresh) {
            return factsDataClassLiveDataResponseModel
        }

        viewModelScope.launch {
            val response =
                mainRepository.getFactsData()

            withContext(Dispatchers.Main) {
                response.collect {
                    _factsDataClassLiveDataResponseModel.postValue((it))
                }
            }
        }
        return factsDataClassLiveDataResponseModel
    }

    /**
     * Methods to check if the internet connection is available or not
     */
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}