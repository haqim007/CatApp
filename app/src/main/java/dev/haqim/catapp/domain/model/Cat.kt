package dev.haqim.catapp.domain.model

data class Cat(
    val name: String,
    val temperament: String,
    val description: String,
    val isHairless: Boolean,
    val energyLevel: Int,
    val imageUrl: String?,
    val id: String,
    val isFavorite: Boolean = false
)


val dummyCats = listOf(
    Cat(
        name = "Abyssinian",
        temperament = "Active, Energetic, Independent, Intelligent, Gentle",
        description = "The Abyssinian is easy to care for, and a joy to have in your home. They’re affectionate cats and love both people and other animals.",
        isHairless = false,
        energyLevel = 3,
        imageUrl = "https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg",
        id = "abys"
    ),
    Cat(
        name = "Abyssinian",
        temperament = "Active, Energetic, Independent, Intelligent, Gentle",
        description = "The Abyssinian is easy to care for, and a joy to have in your home. They’re affectionate cats and love both people and other animals.",
        isHairless = false,
        energyLevel = 2,
        imageUrl = "https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg",
        id = "abys"
    ),
    Cat(
        name = "Abyssinian",
        temperament = "Active, Energetic, Independent, Intelligent, Gentle",
        description = "The Abyssinian is easy to care for, and a joy to have in your home. They’re affectionate cats and love both people and other animals.",
        isHairless = false,
        energyLevel = 4,
        imageUrl = "https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg",
        id = "abys"
    )
)