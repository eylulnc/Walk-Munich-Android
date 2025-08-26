package com.github.eylulnc.walkmunich.core.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PlacesResponse(
    val cityId: Long,
    val cityName: String,
    val updatedAt: String,
    val places: List<Place>
)

@Serializable
data class Place(
    val id: Long,
    val cityId: Long,
    val name: String,
    val category: Category,
    val imageUrl: String,
    val coords: Coordinates? = null,
    val address: String? = null,
    val openingHoursText: String? = null,
    val priceRange: PriceRange? = null,
    val tags: List<String>? = null,
    val otherImages: List<String>? = null,
    val story: PlaceStory? = null,
    val facts: List<Fact>? = null,
    val attribution: List<Attribution>? = null,
    val updatedAt: String
)

@Serializable
data class Coordinates(
    val lat: Double,
    val lon: Double
)

@Serializable
data class PlaceStory(
    val mainTitle: String,
    val subTitle: String? = null,
    val overview: String,
    val highlights: List<Highlight>
)

@Serializable
data class Highlight(
    val title: String,
    val text: String
)

@Serializable
data class Fact(
    val text: String,
    val sourceName: String,
    val sourceUrl: String? = null
)

@Serializable
data class Attribution(
    val text: String,
    val url: String? = null
)

@Serializable
enum class PriceRange {
    LOW, MEDIUM, HIGH
}

