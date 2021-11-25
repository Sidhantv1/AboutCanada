package com.example.aboutcanada

import com.example.aboutcanada.dataclass.FactsDataClass
import retrofit2.Call
import retrofit2.http.GET

/**
 * Api Interface
 */
interface Api {

    @GET(ApiConstants.GET_DATA)
    fun getFactsData(): Call<FactsDataClass>

}