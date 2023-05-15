package dev.haqim.catapp.data.mechanism

import kotlinx.coroutines.flow.*

abstract class NetworkBoundResource<ResultType, RequestType> {
    private val result: Flow<Resource<ResultType>>
        get() = flow {
            emit(Resource.Loading())
            val apiResponse = createCall().first()
            if(apiResponse.isSuccess){
                apiResponse.getOrNull()?.let {data ->
                    saveCallResult(data)
                    emitAll(loadResult(data).map { Resource.Success(it) })
                }
            }else if(apiResponse.isFailure){
                try{
                    emitAll(loadLocalData().map { Resource.Success(it) })
                }catch (e: Exception) {
                    onFetchFailed()
                    emit(
                        Resource.Error(
                            apiResponse.exceptionOrNull()?.localizedMessage ?: "Unknown error"
                        )
                    )
                }
            }
        }

    protected abstract fun loadResult(data: RequestType): Flow<ResultType>

    protected open fun onFetchFailed() {}

    protected abstract suspend fun createCall(): Flow<Result<RequestType>>

    protected open fun loadFromNetwork(data: RequestType): Flow<ResultType> = flow{}

    protected open fun loadLocalData(): Flow<ResultType> = flow {}

    protected abstract suspend fun saveCallResult(data: RequestType)

    // TODO: add handler onSaveFailed

    fun asFlow(): Flow<Resource<ResultType>> = result
}