package dev.haqim.catapp.data.local.room

import androidx.room.*
import dev.haqim.catapp.data.local.entity.CatEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CatDao{
    @Query("SELECT * FROM cats")
    fun getCats(): Flow<List<CatEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCats(vararg cats: CatEntity)

    @Query("UPDATE cats SET isFavorite = :isFavorite where id = :id")
    suspend fun updateFavorite(id: String, isFavorite: Boolean)

    @Query("SELECT * FROM cats WHERE id = :id")
    fun getCatById(id: String): Flow<CatEntity?>

    @Query("SELECT * FROM cats WHERE name LIKE :name")
    fun findCatByName(name: String): Flow<List<CatEntity>>

    @Query("SELECT * FROM cats WHERE name LIKE :name AND isFavorite = true")
    fun getFavoriteCats(name: String): Flow<List<CatEntity>>
}