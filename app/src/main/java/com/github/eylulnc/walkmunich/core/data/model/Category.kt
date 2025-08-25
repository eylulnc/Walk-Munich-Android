package com.github.eylulnc.walkmunich.core.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Attractions
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Museum
import androidx.compose.material.icons.filled.NaturePeople
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.ui.graphics.vector.ImageVector
import com.github.eylulnc.walkmunich.R
import kotlinx.serialization.Serializable

@Serializable
enum class Category {
    LANDMARK, MUSEUM, VIEWPOINT, COFFEE, FOOD
}

data class CategoryUi(
    val titleResource: Int,
    val icon: ImageVector
)

fun Category.toUi(): CategoryUi = when (this) {
    Category.LANDMARK -> CategoryUi(R.string.category_attraction, Icons.Filled.Attractions)
    Category.MUSEUM -> CategoryUi(R.string.category_museum, Icons.Filled.Museum)
    Category.VIEWPOINT -> CategoryUi(R.string.category_viewpoint, Icons.Filled.NaturePeople)
    Category.COFFEE -> CategoryUi(R.string.category_coffee, Icons.Filled.LocalCafe)
    Category.FOOD -> CategoryUi(R.string.category_food, Icons.Filled.Restaurant)
}