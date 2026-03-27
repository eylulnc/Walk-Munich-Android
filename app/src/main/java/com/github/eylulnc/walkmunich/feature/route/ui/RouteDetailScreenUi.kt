package com.github.eylulnc.walkmunich.feature.route.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.eylulnc.walkmunich.R
import com.github.eylulnc.walkmunich.core.data.model.RouteDetail
import com.github.eylulnc.walkmunich.core.data.model.RouteSegment
import com.github.eylulnc.walkmunich.core.data.model.RouteStop
import com.github.eylulnc.walkmunich.core.data.model.toUi
import com.github.eylulnc.walkmunich.core.ui.composable.ErrorState
import com.github.eylulnc.walkmunich.core.ui.composable.LoadingState
import com.github.eylulnc.walkmunich.core.ui.theme.Spacing
import com.github.eylulnc.walkmunich.core.ui.theme.TypographySizes
import com.github.eylulnc.walkmunich.core.ui.util.ImageResolver
import com.github.eylulnc.walkmunich.feature.route.viewmodel.RouteDetailViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun RouteDetailScreenUi(
    viewModel: RouteDetailViewModel = koinViewModel(),
    onPlaceItemClick: (Long, String?) -> Unit,
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    when {
        state.isLoading -> LoadingState()
        state.error != null -> ErrorState(errorMessage = state.error)
        state.routeDetail != null -> RouteDetailContent(
            detail = state.routeDetail!!,
            onPlaceItemClick = onPlaceItemClick,
            onBackClick = onBackClick
        )
    }
}

@Composable
private fun RouteDetailContent(
    detail: RouteDetail,
    onPlaceItemClick: (Long, String?) -> Unit,
    onBackClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            val heroImage = ImageResolver.resolveDrawable(detail.imageUrl) ?: R.drawable.hero_munich
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {
                Image(
                    painter = painterResource(heroImage),
                    contentDescription = detail.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Black.copy(alpha = 0.5f), Color.Transparent)
                            )
                        )
                )
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = Spacing.NegativeCardOffset),
                shape = RoundedCornerShape(topStart = Spacing.CardCornerRadius, topEnd = Spacing.CardCornerRadius),
                color = MaterialTheme.colorScheme.background
            ) {
                Column {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = Spacing.Large)
                            .padding(top = Spacing.Large, bottom = Spacing.Medium)
                    ) {
                        Text(
                            text = detail.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = TypographySizes.title,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        detail.summary?.let {
                            Spacer(Modifier.height(Spacing.Small))
                            Text(
                                text = it,
                                fontSize = TypographySizes.medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = Spacing.Large),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )

                    Row(
                        modifier = Modifier
                            .padding(horizontal = Spacing.Large, vertical = Spacing.Medium),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.Small)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Map,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            text = stringResource(R.string.itinerary),
                            fontWeight = FontWeight.Bold,
                            fontSize = TypographySizes.large,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    detail.segments.forEach { segment ->
                        ItinerarySegment(
                            segment = segment,
                            displaySubtitle = detail.segments.size > 1,
                            onPlaceItemClick = onPlaceItemClick
                        )
                    }

                    Spacer(Modifier.height(Spacing.Large))
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = Spacing.Small, vertical = Spacing.ExtraSmall)
        ) {
            HeroIconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun HeroIconButton(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .size(Spacing.ActionIconSize)
            .background(Color.Black.copy(alpha = 0.35f), CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
private fun ItinerarySegment(
    segment: RouteSegment,
    displaySubtitle: Boolean,
    onPlaceItemClick: (Long, String?) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = Spacing.Large, vertical = Spacing.Medium)) {
        if (displaySubtitle) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.CalendarMonth,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = Spacing.Small)
                )
                Text(
                    text = segment.title,
                    fontSize = TypographySizes.subtitle,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(Modifier.height(Spacing.Medium))
        }

        segment.stops.forEachIndexed { index, stop ->
            RouteStopCard(
                stop = stop,
                stepNumber = index + 1,
                onClick = { onPlaceItemClick(stop.placeId, null) }
            )
        }
    }
}

@Composable
fun RouteStopCard(
    stop: RouteStop,
    stepNumber: Int,
    onClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stepNumber.toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = TypographySizes.small
                )
            }

            Spacer(Modifier.width(Spacing.Medium))

            val imageResId = ImageResolver.resolveDrawable(stop.name.lowercase())

            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = onClick),
                shape = RoundedCornerShape(Spacing.CornerRadius),
                elevation = CardDefaults.cardElevation(defaultElevation = Spacing.None),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                border = BorderStroke(
                    Spacing.BorderStroke,
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                )
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .height(Spacing.CardHeightSmall)
                            .fillMaxWidth()
                    ) {
                        imageResId?.let {
                            Image(
                                painter = painterResource(id = it),
                                contentDescription = stop.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Box(
                            modifier = Modifier
                                .padding(Spacing.Small)
                                .align(Alignment.TopEnd)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.2f))
                                .padding(Spacing.Small)
                        ) {
                            Icon(
                                imageVector = stop.category.toUi().icon,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }

                    Column(modifier = Modifier.padding(Spacing.Small)) {
                        Text(
                            text = stop.name,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = TypographySizes.body,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }

    Spacer(Modifier.height(Spacing.Medium))
}
