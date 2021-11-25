package com.example.aboutcanada

import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {

    fun factsDataObserver() = mainRepository.countryDataClassLiveData

    fun loadFactsApi() {
        viewModelScope.launch {
            mainRepository.loadFactsApi()
        }
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