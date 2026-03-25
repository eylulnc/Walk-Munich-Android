package com.github.eylulnc.walkmunich.core.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.eylulnc.walkmunich.R
import com.github.eylulnc.walkmunich.core.data.model.Category
import com.github.eylulnc.walkmunich.core.data.model.Place
import com.github.eylulnc.walkmunich.core.ui.theme.OrangeMain
import com.github.eylulnc.walkmunich.core.ui.theme.Spacing
import com.github.eylulnc.walkmunich.core.ui.theme.TypographySizes

@Composable
fun PlaceCard(
    place: Place,
    onPlaceClick: () -> Unit,
    isFavorite: Boolean = false,
    onFavoriteClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .width(Spacing.CardWidthLarge)
            .height(Spacing.CardHeightPoster)
            .clickable(onClick = onPlaceClick),
        shape = RoundedCornerShape(Spacing.CornerRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = Spacing.None)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background image (falls back to gradient placeholder when no asset exists)
            PlaceImage(
                imageUrl = place.imageUrl,
                contentDescription = place.name,
                modifier = Modifier.fillMaxSize()
            )

            // Gradient overlay (transparent → black, bottom half)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colorStops = arrayOf(
                                0.0f to Color.Transparent,
                                0.45f to Color.Transparent,
                                1.0f to Color.Black.copy(alpha = 0.65f)
                            )
                        )
                    )
            )

            // Favorite button (top-right)
            FavoriteButton(
                isFavorite = isFavorite,
                onClick = onFavoriteClick
            )

            // Category badge + place name (bottom-left)
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(Spacing.Small + Spacing.ExtraSmall)
            ) {
                CategoryBadge(category = place.category)
                Text(
                    text = place.name,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = TypographySizes.body,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = Spacing.Tiny)
                )
            }
        }
    }
}

@Composable
private fun CategoryBadge(category: Category) {
    val label = when (category) {
        Category.LANDMARK -> stringResource(R.string.category_attraction)
        Category.MUSEUM -> stringResource(R.string.category_museum)
        Category.VIEWPOINT -> stringResource(R.string.category_viewpoint)
        Category.COFFEE -> stringResource(R.string.category_coffee)
        Category.FOOD -> stringResource(R.string.category_food)
    }

    Box(
        modifier = Modifier
            .background(
                color = OrangeMain.copy(alpha = 0.9f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = Spacing.Small, vertical = Spacing.Tiny)
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
