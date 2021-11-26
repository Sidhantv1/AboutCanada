package com.example.aboutcanada.viewmodel

import android.content.Context
import android.net.ConnectivityManager
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

class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {

    private var _factsDataClassLiveDataResponseModel =
        MutableLiveData<DataResult<FactsDataClassResponseModel>>()

    var factsDataClassLiveDataResponseModel: LiveData<DataResult<FactsDataClassResponseModel>> =
        _factsDataClassLiveDataResponseModel

    fun getFactsData(isPulledToRefresh: Boolean = false): LiveData<DataResult<FactsDataClassResponseModel>> {

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
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!
            .isConnected
    }
}