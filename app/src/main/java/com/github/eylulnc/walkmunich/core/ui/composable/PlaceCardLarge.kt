package com.github.eylulnc.walkmunich.core.ui.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.github.eylulnc.walkmunich.core.data.model.Place
import com.github.eylulnc.walkmunich.core.ui.theme.Spacing
import com.github.eylulnc.walkmunich.core.ui.theme.TypographySizes
import com.github.eylulnc.walkmunich.core.ui.util.ImageResolver

@Composable
fun PlaceCardLarge(
    place: Place,
    onPlaceClick: () -> Unit,
    isFavorite: Boolean = false,
    onFavoriteClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(Spacing.CardHeightMedium)
            .clickable(onClick = onPlaceClick),
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
        Row(modifier = Modifier.fillMaxWidth()) {
            val imageResId = ImageResolver.resolveDrawable(place.imageUrl)

            // 🖼️ Image section
            Box(
                modifier = Modifier
                    .height(Spacing.CardHeightMedium)
                    .weight(0.4f)
            ) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = "Place Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth()
                )

                FavoriteButton(
                    isFavorite = isFavorite,
                    onClick = onFavoriteClick
                )
            }

            // 📝 Text section
            Column(
                modifier = Modifier
                    .weight(0.6f)
                    .padding(horizontal = Spacing.Medium, vertical = Spacing.Small),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = place.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = TypographySizes.medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = place.category.name,
                    fontSize = TypographySizes.small,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = Spacing.Small)
                )
            }
        }
    }
}
