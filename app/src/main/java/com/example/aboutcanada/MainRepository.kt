package com.example.aboutcanada

import androidx.lifecycle.MutableLiveData
import com.example.aboutcanada.dataclass.FactsDataClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainRepository {

    val factsDataClassLiveData: MutableLiveData<FactsDataClass> = MutableLiveData()

    fun loadFactsApi() {
        GlobalScope.launch(Dispatchers.IO) {
            val api = RetrofitClientInstance.getRetrofitInstance().create(Api::class.java)

            // Api Request parameter passed in query
            val call = api.getFactsData()

            call.enqueue(object : Callback<FactsDataClass> {
                // Failure Response
                override fun onFailure(call: Call<FactsDataClass>, t: Throwable) {}

                // Success Response
                override fun onResponse(
                    call: Call<FactsDataClass>,
                    response: Response<FactsDataClass>
                ) {
                    factsDataClassLiveData.value = response.body()
                }
            })
        }
    }
}
