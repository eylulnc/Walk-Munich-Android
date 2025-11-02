package com.github.eylulnc.walkmunich.feature.route.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
                Column(
                    modifier = contentMod
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = Spacing.Medium)
                ) {
                    val detail = state.routeDetail!!
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(Spacing.Large)
                    ) {
                        detail.segments.forEach { segment ->
                            ItinerarySegment(
                                segment = segment,
                                displaySubtitle = detail.segments.size != 1,
                                onPlaceItemClick = onPlaceItemClick
                            )
                        }
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
    Column(
        modifier = Modifier.padding(Spacing.Medium),
        verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
    ) {
        if (displaySubtitle) {
            Text(
                text = segment.title,
                fontSize = TypographySizes.medium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        segment.stops.forEachIndexed { stopIndex, stop ->
            val nextStop = segment.stops.getOrNull(stopIndex + 1)
            val subTitle = nextStop?.let { stringResource(R.string.next_stop, it.name)}
            RouteStopItem(
                stop = stop,
                onClick = { onPlaceItemClick(stop.placeId, subTitle) }
            )
        }
    }
}

@Composable
private fun RouteStopItem(
    stop: RouteStop,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Stop number circle
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(OrangeMain),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stop.ord.toString(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = TypographySizes.medium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(Spacing.Small))

            // Stop details card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = Spacing.Small)
                    .clickable(onClick = onClick),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(Spacing.CornerRadius),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline
                )
            ) {
                val categoryUi = stop.category.toUi()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Spacing.Medium),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = categoryUi.icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(Spacing.Small))

                    Text(
                        text = stop.name,
                        fontSize = TypographySizes.medium,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
