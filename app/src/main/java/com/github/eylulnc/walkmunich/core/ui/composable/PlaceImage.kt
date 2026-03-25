package com.github.eylulnc.walkmunich.core.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.github.eylulnc.walkmunich.core.ui.theme.BlueTeal
import com.github.eylulnc.walkmunich.core.ui.theme.OrangeMain
import com.github.eylulnc.walkmunich.core.ui.util.ImageResolver

/**
 * Displays a place image by name, falling back to [PlaceholderImage] when
 * no matching drawable is registered in [ImageResolver].
 */
@Composable
fun PlaceImage(
    imageUrl: String?,
    contentDescription: String? = null,
    modifier: Modifier = Modifier
) {
    val drawableId = ImageResolver.resolveDrawable(imageUrl)
    if (drawableId != null) {
        Image(
            painter = painterResource(id = drawableId),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = modifier
        )
    } else {
        PlaceholderImage(modifier = modifier)
    }
}

/**
 * Orange → teal diagonal gradient with a faint photo icon.
 * Matches the iOS PlaceholderImageView used on the same screen.
 */
@Composable
fun PlaceholderImage(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(
            Brush.linearGradient(
                colors = listOf(
                    OrangeMain.copy(alpha = 0.6f),
                    BlueTeal.copy(alpha = 0.6f)
                )
            )
        ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.Image,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.5f),
            modifier = Modifier.size(48.dp)
        )
    }
}
