package com.example.aboutcanada.networking

import com.example.aboutcanada.dataclass.FactsDataClassResponseModel
import retrofit2.Response
import retrofit2.http.GET

/**
 * Api Interface
 */
interface Api {

    @GET(ApiConstants.GET_DATA)
    suspend fun getFactsData(): Response<FactsDataClassResponseModel>

}