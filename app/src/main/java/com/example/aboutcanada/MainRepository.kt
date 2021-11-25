package com.example.aboutcanada

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.aboutcanada.dataclass.CountryDataClass
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class MainRepository {

    val countryDataClassLiveData: MutableLiveData<CountryDataClass> = MutableLiveData()

    fun loadFactsApi() {
        GlobalScope.launch(Dispatchers.IO) {
            val api = RetrofitClientInstance.getRetrofitInstance().create(Api::class.java)

            // Api Request parameter passed in query
            val call = api.getFactsData()

            call.enqueue(object : Callback<CountryDataClass> {
                // Failure Response
                override fun onFailure(call: Call<CountryDataClass>, t: Throwable) {}

                // Success Response
                override fun onResponse(
                    call: Call<CountryDataClass>,
                    response: Response<CountryDataClass>
                ) {
                    countryDataClassLiveData.value = response.body()
                }
            })
        }
    }
}
