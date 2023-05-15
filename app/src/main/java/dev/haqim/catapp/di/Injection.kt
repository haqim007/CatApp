package dev.haqim.catapp.di

import android.content.Context
import dev.haqim.catapp.data.local.room.CatDao
import dev.haqim.catapp.data.local.room.CatDatabase
import dev.haqim.catapp.data.remote.RemoteDataSource
import dev.haqim.catapp.data.remote.retrofit.ApiConfig
import dev.haqim.catapp.data.remote.retrofit.ApiService
import dev.haqim.catapp.data.repository.CatRepository

object Injection{
    fun provideRepository(context: Context): CatRepository{
        return CatRepository(provideRemoteDataSource(), provideLocalDataSource(context))
    }

    fun provideRemoteDataSource(): RemoteDataSource =
        RemoteDataSource(provideApiService())

    fun provideApiService(): ApiService = ApiConfig.getApiService()

    fun provideLocalDataSource(context: Context): CatDao{
        val database = CatDatabase.getInstance(context)
        return database.catDao()
    }
}