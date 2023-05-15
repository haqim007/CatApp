package dev.haqim.catapp.data.repository

import dev.haqim.catapp.BuildConfig
import dev.haqim.catapp.data.local.entity.CatEntity
import dev.haqim.catapp.data.local.entity.toModel
import dev.haqim.catapp.data.local.room.CatDao
import dev.haqim.catapp.data.mechanism.LocalBoundResource
import dev.haqim.catapp.data.mechanism.NetworkBoundResource
import dev.haqim.catapp.data.mechanism.Resource
import dev.haqim.catapp.data.remote.RemoteDataSource
import dev.haqim.catapp.data.remote.response.CatResponseItem
import dev.haqim.catapp.data.remote.response.toEntity
import dev.haqim.catapp.domain.model.Cat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class CatRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: CatDao
){
    fun getCats(searchQuery: String = ""): Flow<Resource<List<Cat>?>> {
        return object : NetworkBoundResource<List<Cat>?, List<CatResponseItem>>(){

            override suspend fun createCall(): Flow<Result<List<CatResponseItem>>> {
                return remoteDataSource.getCats(BuildConfig.API_KEY)
            }

            override fun loadLocalData(): Flow<List<Cat>?> {
                return if (searchQuery != "")
                    localDataSource.findCatByName("%$searchQuery%").map { it.toModel() }
                else
                    localDataSource.getCats().map { it.toModel() }
            }

            override fun loadResult(data: List<CatResponseItem>): Flow<List<Cat>?> {
                return loadLocalData()
            }

            override suspend fun saveCallResult(data: List<CatResponseItem>) {
                localDataSource.insertCats(*data.toEntity())
            }

        }.asFlow()
    }

    fun getCatById(id: String): Flow<Resource<Cat?>> {
        return object: LocalBoundResource<Cat?, CatEntity?>() {

            override suspend fun createCall(): Flow<CatEntity?> {
                return localDataSource
                    .getCatById(id)
                    .distinctUntilChanged()
            }

            override fun loadResult(data: CatEntity?): Flow<Cat?> {
                val cat = data?.toModel()
                return flowOf(cat)
            }

        }.asFlow()
    }

    suspend fun setToFavorite(id: String, value: Boolean){
        localDataSource.updateFavorite(id, value)
    }

    fun getFavoriteCats(searchQuery: String = ""): Flow<Resource<List<Cat>?>> {
        return object: LocalBoundResource<List<Cat>?, List<CatEntity>?>() {
            override suspend fun createCall(): Flow<List<CatEntity>?> {
                return localDataSource.getFavoriteCats("%$searchQuery%")
            }

            override fun loadResult(data: List<CatEntity>?): Flow<List<Cat>?> {
                val cat = data?.toModel()
                return flowOf(cat)
            }


        }.asFlow()
    }
}