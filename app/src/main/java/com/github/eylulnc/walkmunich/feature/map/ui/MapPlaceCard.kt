package com.github.eylulnc.walkmunich.feature.map.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
fun MapPlaceCard(
    place: Place,
    onPlaceClick: () -> Unit
) {
    val imageResId = ImageResolver.resolveDrawable(place.imageUrl)

    OutlinedCard(
        modifier = Modifier.width(Spacing.CardHeightMedium),
        shape = RoundedCornerShape(Spacing.CornerRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = Spacing.None),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        border = BorderStroke(
            Spacing.Tiny,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        )
    ) {
        Box(
            modifier = Modifier
                .height(Spacing.CardHeightMedium)
                .clickable(onClick = onPlaceClick)
        ) {
            imageResId?.let {
                Image(
                    painter = painterResource(id = it),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = Spacing.Small, end = Spacing.ExtraSmall, top = Spacing.ExtraSmall, bottom = Spacing.ExtraSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = place.name,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = TypographySizes.small,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
