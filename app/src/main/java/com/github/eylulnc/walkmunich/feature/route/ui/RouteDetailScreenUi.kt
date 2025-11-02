package com.github.eylulnc.walkmunich.feature.route.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.eylulnc.walkmunich.R
import com.github.eylulnc.walkmunich.core.data.model.RouteSegment
import com.github.eylulnc.walkmunich.core.data.model.RouteStop
import com.github.eylulnc.walkmunich.core.data.model.toUi
import com.github.eylulnc.walkmunich.core.ui.composable.ErrorState
import com.github.eylulnc.walkmunich.core.ui.composable.LoadingState
import com.github.eylulnc.walkmunich.core.ui.composable.WMTopAppBarScreen
import com.github.eylulnc.walkmunich.core.ui.theme.OrangeMain
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

    WMTopAppBarScreen(
        title = state.routeDetail?.title ?: stringResource(R.string.route_details),
        onBack = onBackClick
    ) { contentMod ->
        when {
            state.isLoading -> LoadingState()
            state.error != null -> ErrorState(errorMessage = state.error)
            state.routeDetail != null -> {
                val detail = state.routeDetail!!

                Column(
                    modifier = contentMod
                        .verticalScroll(rememberScrollState())
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    // --- Hero Image ---
                    val heroImage = ImageResolver.resolveDrawable(detail.imageUrl)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
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
                                        listOf(
                                            Color.Black.copy(alpha = 0.5f),
                                            Color.Transparent
                                        )
                                    )
                                )
                        )
                    }

                    // --- Description ---
                    detail.summary?.let {
                        Text(
                            text = it,
                            fontSize = TypographySizes.medium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .padding(Spacing.Large)
                        )
                    }

                    // --- Itinerary ---
                    Text(
                        text = stringResource(R.string.itinerary),
                        fontSize = TypographySizes.subtitle,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(
                            horizontal = Spacing.Large,
                            vertical = Spacing.Small
                        )
                    )

                    detail.segments.forEach { segment ->
                        ItinerarySegment(
                            segment = segment,
                            displaySubtitle = detail.segments.size > 1,
                            onPlaceItemClick = onPlaceItemClick
                        )
                    }
                }
            }
        }
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
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(Modifier.height(Spacing.Medium))

        segment.stops.forEachIndexed { index, stop ->
            RouteStopCard(
                stop = stop,
                stepNumber = index + 1,
                isLast = index == segment.stops.lastIndex,
                onClick = { onPlaceItemClick(stop.placeId, null) }
            )
        }

    }
}

@Composable
fun RouteStopCard(
    stop: RouteStop,
    stepNumber: Int,
    isLast: Boolean,
    onClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        // --- Dashed line connecting to next stop ---
        if (!isLast) {
            Canvas(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(2.dp)
                    .align(Alignment.TopStart)
                    .offset(x = 15.dp, y = 40.dp)
            ) {
                drawLine(
                    color = OrangeMain.copy(alpha = 0.4f),
                    start = Offset(0f, 0f),
                    end = Offset(0f, size.height),
                    strokeWidth = 2.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                )
            }
        }

        Row(verticalAlignment = Alignment.Top) {
            // --- Step Number Circle ---
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
                        Image(
                            painter = painterResource(id = imageResId),
                            contentDescription = stop.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        Box(
                            modifier = Modifier
                                .padding(Spacing.Small)
                                .align(Alignment.TopEnd)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.3f))
                                .padding(Spacing.ExtraSmall)
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

