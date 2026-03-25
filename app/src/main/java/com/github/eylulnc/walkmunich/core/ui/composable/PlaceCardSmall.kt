package com.github.eylulnc.walkmunich.core.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.github.eylulnc.walkmunich.R
import com.github.eylulnc.walkmunich.core.data.model.Category
import com.github.eylulnc.walkmunich.core.data.model.Place
import com.github.eylulnc.walkmunich.core.ui.theme.Spacing
import com.github.eylulnc.walkmunich.core.ui.theme.TypographySizes

@Composable
fun PlaceCardSmall(
    place: Place,
    onPlaceClick: () -> Unit,
    isFavorite: Boolean = false,
    onFavoriteClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onPlaceClick),
        shape = RoundedCornerShape(Spacing.CornerRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = Spacing.None),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        // Image with gradient overlay
        Box(modifier = Modifier.height(Spacing.CardHeightSmall)) {
            PlaceImage(
                imageUrl = place.imageUrl,
                contentDescription = place.name,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colorStops = arrayOf(
                                0.0f to Color.Transparent,
                                0.45f to Color.Transparent,
                                1.0f to Color.Black.copy(alpha = 0.55f)
                            )
                        )
                    )
            )

            FavoriteButton(
                isFavorite = isFavorite,
                onClick = onFavoriteClick
            )
        }

        // Name + category label — fixed height ensures all grid items are uniform
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = Spacing.Small, vertical = Spacing.ExtraSmall + Spacing.Tiny)
        ) {
            Text(
                text = place.name,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = TypographySizes.small,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = categoryLabel(place.category),
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                fontSize = TypographySizes.small,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun categoryLabel(category: Category): String = when (category) {
    Category.LANDMARK -> stringResource(R.string.category_attraction)
    Category.MUSEUM -> stringResource(R.string.category_museum)
    Category.VIEWPOINT -> stringResource(R.string.category_viewpoint)
    Category.COFFEE -> stringResource(R.string.category_coffee)
    Category.FOOD -> stringResource(R.string.category_food)
}
