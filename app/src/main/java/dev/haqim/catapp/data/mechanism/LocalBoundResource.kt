package dev.haqim.catapp.data.mechanism

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

abstract class LocalBoundResource<ResultType, RequestType> {
    private val result: Flow<Resource<ResultType>>
        get() = flow {
            emit(Resource.Loading())
            try {
//                emitAll(
//                    loadResult(createCall().first()).map { Resource.Success(it) }
//                )
                createCall().collect{ data ->
                    emitAll(loadResult(data).map { Resource.Success(it) })
                }
            }catch (e: Exception){
                onFailed()
                emit(
                    Resource.Error(
                        message = e.localizedMessage ?: "Unknown error"
                    )
                )
            }

        }

    protected abstract suspend fun createCall(): Flow<RequestType>

    protected abstract fun loadResult(data: RequestType?): Flow<ResultType>

    protected open fun onFailed() {}

    fun asFlow(): Flow<Resource<ResultType>> = result
}