package dev.haqim.catapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.haqim.catapp.domain.model.Cat

@Entity(tableName = "cats")
data class CatEntity(
    @field:ColumnInfo(name = "id")
    @field:PrimaryKey
    val id: String,
    @field:ColumnInfo(name = "name")
    val name: String,
    @field:ColumnInfo(name = "temperament")
    val temperament: String,
    @field:ColumnInfo(name = "description")
    val description: String,
    @field:ColumnInfo(name = "isHairless")
    val isHairless: Boolean,
    @field:ColumnInfo(name = "energyLevel")
    val energyLevel: Int,
    @field:ColumnInfo(name = "imageUrl")
    val imageUrl: String?,
    @field:ColumnInfo(name = "isFavorite")
    val isFavorite: Boolean = false,
)

fun List<CatEntity>.toModel(): List<Cat>{
    return this.map {
        Cat(
            name = it.name,
            id = it.id,
            temperament = it.temperament,
            description = it.description,
            isHairless = it.isHairless,
            energyLevel = it.energyLevel,
            imageUrl = it.imageUrl,
            isFavorite = it.isFavorite
        )
    }
}

fun CatEntity.toModel(): Cat{
    return Cat(
        name = this.name,
        id = this.id,
        temperament = this.temperament,
        description = this.description,
        isHairless = this.isHairless,
        energyLevel = this.energyLevel,
        imageUrl = this.imageUrl,
        isFavorite = this.isFavorite
    )
}