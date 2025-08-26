package com.github.eylulnc.walkmunich.core.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Favorites(
    val placeIds: List<Long>
)
