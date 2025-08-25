package com.github.eylulnc.walkmunich.core.data.model

import kotlinx.serialization.Serializable

@Serializable
data class City(
    val id: Long,
    val name: String,
    val country: String,
    val heroImage: HeroImage,
    val categoriesEnabled: List<CategoryType>,
    val updatedAt: String
)

@Serializable
data class HeroImage(
    val imageUrl: String
)

@Serializable
enum class CategoryType {
    LANDMARK, MUSEUM, VIEWPOINT, COFFEE, FOOD
}