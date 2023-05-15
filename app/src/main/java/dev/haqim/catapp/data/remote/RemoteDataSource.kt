package dev.haqim.catapp.data.remote

import com.google.gson.Gson
import dev.haqim.catapp.data.remote.response.BasicResponse
import dev.haqim.catapp.data.remote.response.CatResponseItem
import dev.haqim.catapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException

class RemoteDataSource(private val service: ApiService){
    suspend fun getCats(token: String): Flow<Result<List<CatResponseItem>>> {
        return flow{
            try {
                val response = service.getCats(apiKey = token)
                emit(Result.success(response))
            }catch (e: Exception){
                val error = (e as? HttpException)?.response()?.errorBody()?.string()
                generateError(error, e)
            }
        }.flowOn(Dispatchers.IO)
    }

    private suspend fun FlowCollector<Result<List<CatResponseItem>>>.generateError(
        error: String?,
        e: Exception,
    ) {
        try {
            val response = Gson().fromJson(error, BasicResponse::class.java)
            emit(Result.failure(Throwable(message = response.message)))
        } catch (exception: Exception) {
            emit(Result.failure(e))
        }
    }
}