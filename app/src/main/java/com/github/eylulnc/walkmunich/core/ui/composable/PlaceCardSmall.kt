package com.github.eylulnc.walkmunich.core.ui.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.github.eylulnc.walkmunich.core.data.model.Place
import com.github.eylulnc.walkmunich.core.ui.theme.Spacing
import com.github.eylulnc.walkmunich.core.ui.theme.TypographySizes
import com.github.eylulnc.walkmunich.core.ui.util.ImageResolver

@Composable
fun PlaceCardSmall(
    place: Place,
    onPlaceClick: () -> Unit,
    isFavorite: Boolean = false,
    onFavoriteClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier.width(Spacing.CardHeightMedium),
        shape = RoundedCornerShape(Spacing.CornerRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = Spacing.None),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        border = BorderStroke(
            Spacing.BorderStroke,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        )
    ) {
        val imageResId = ImageResolver.resolveDrawable(place.imageUrl)

        Box(
            modifier = Modifier
                .height(Spacing.CardHeightSmall)
                .clickable(onClick = onPlaceClick)
        ) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = "Place Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            FavoriteButton(
                isFavorite = isFavorite,
                onClick = onFavoriteClick
            )
        }

        Column(
            modifier = Modifier.padding(
                horizontal = Spacing.Small,
                vertical = Spacing.ExtraSmall
            )
        ) {
            Text(
                text = place.name,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = TypographySizes.small,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
