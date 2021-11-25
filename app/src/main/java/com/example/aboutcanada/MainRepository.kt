package com.example.aboutcanada

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.aboutcanada.dataclass.CountryDataClass
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException

class MainRepository {

    val countryDataClassLiveData: MutableLiveData<CountryDataClass> = MutableLiveData()

    fun parseJSONData(context: Context) {
        GlobalScope.launch(Dispatchers.IO) {
            val jsonFileString = "aboutcanada.json".getJsonDataFromAsset(context)
            val listPersonType = object : TypeToken<CountryDataClass>() {}.type
            val countryDataClass: CountryDataClass = Gson().fromJson(jsonFileString, listPersonType)
            countryDataClassLiveData.postValue(countryDataClass)
        }
    }

    private fun String.getJsonDataFromAsset(context: Context): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(this).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

}
