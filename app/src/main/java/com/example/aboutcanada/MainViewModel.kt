package com.example.aboutcanada

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application

    private val mainRepository by lazy {
        MainRepository()
    }

    fun countryDataObserver() = mainRepository.countryDataClassLiveData

    fun fetchFactsDataFromJson() {
        viewModelScope.launch {
            mainRepository.parseJSONData(context)
        }
    }
}