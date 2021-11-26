package com.example.aboutcanada.repository

import com.example.aboutcanada.dataclass.FactsDataClassResponseModel
import com.example.aboutcanada.networking.Api
import com.example.aboutcanada.networking.DataResult
import com.example.aboutcanada.networking.NetworkOnlineDataRepo
import com.example.aboutcanada.networking.RetrofitClientInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

class MainRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getFactsData(): Flow<DataResult<FactsDataClassResponseModel>> {
        val retrofitClientInstance =
            RetrofitClientInstance.getRetrofitInstance().create(Api::class.java)
        return object :
            NetworkOnlineDataRepo<FactsDataClassResponseModel, FactsDataClassResponseModel>() {
            override suspend fun fetchDataFromRemoteSource(): Response<FactsDataClassResponseModel> {
                return retrofitClientInstance.getFactsData()
            }
        }.asFlow().flowOn(Dispatchers.IO)
    }
}
