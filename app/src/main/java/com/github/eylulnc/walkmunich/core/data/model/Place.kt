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
    val category: CategoryType,
    val coords: Coordinates? = null,
    val address: String? = null,
    val openingHoursText: String? = null,
    val priceRange: PriceRange? = null,
    val tags: List<String>? = null,
    val images: List<PlaceImage>? = null,
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
data class PlaceImage(
    val imageUrl: String,
    val thumbUrl: String? = null,
    val caption: String? = null
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

@Serializable
data class PlaceMin(
    val id: Long,
    val name: String,
    val category: CategoryType,
    val imageUrl: String
)

@Serializable
data class PlacesMinResponse(
    val cityId: Long,
    val cityName: String,
    val updatedAt: String,
    val places: List<PlaceMin>
)


