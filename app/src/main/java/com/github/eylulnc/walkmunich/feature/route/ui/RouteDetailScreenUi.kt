package com.github.eylulnc.walkmunich.feature.route.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.eylulnc.walkmunich.core.data.model.RouteDetail
import com.github.eylulnc.walkmunich.core.data.model.RouteSegment
import com.github.eylulnc.walkmunich.core.data.model.RouteStop
import com.github.eylulnc.walkmunich.core.data.model.toUi
import com.github.eylulnc.walkmunich.core.ui.theme.OrangeMain
import com.github.eylulnc.walkmunich.core.ui.theme.Spacing
import com.github.eylulnc.walkmunich.core.ui.theme.TypographySizes
import com.github.eylulnc.walkmunich.feature.route.viewmodel.RouteDetailViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteDetailScreenUi(
    routeId: Long,
    onBackClick: () -> Unit
) {
    val viewModel: RouteDetailViewModel = koinViewModel<RouteDetailViewModel> {
        parametersOf(routeId)
    }

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = state.routeDetail?.title ?: "Route Details",
                    fontSize = TypographySizes.large,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = OrangeMain
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
                titleContentColor = Color.Black
            )
        )

        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = OrangeMain)
                }
            }

            state.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error loading route",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Red
                        )
                        Spacer(modifier = Modifier.height(Spacing.Small))
                        Text(
                            text = state.error ?: "Unknown error",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }
            }

            state.routeDetail != null -> {
                RouteDetailContent(routeDetail = state.routeDetail!!)
            }
        }
    }
}

@Composable
private fun RouteDetailContent(routeDetail: RouteDetail) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Spacing.Medium)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Spacing.Large)
        ) {
            routeDetail.segments.forEachIndexed { segmentIndex, segment ->
                ItinerarySegment(
                    segment = segment,
                    displaySubtitle = routeDetail.segments.size != 1

                )
            }
        }
    }
}


@Composable
private fun ItinerarySegment(
    segment: RouteSegment,
    displaySubtitle: Boolean
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
                color = Color.Black
            )
        }

        segment.stops.forEachIndexed { stopIndex, stop ->
            RouteStopItem(
                stop = stop
            )
        }
    }
}

@Composable
private fun RouteStopItem(
    stop: RouteStop
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
                    color = Color.White,
                    fontSize = TypographySizes.medium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(Spacing.Small))

            // Stop details card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = Spacing.Small),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(Spacing.CornerRadius),
                border = BorderStroke(
                    width = 1.dp,
                    color = Color.LightGray
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
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(Spacing.Small))

                    Text(
                        text = stop.name,
                        fontSize = TypographySizes.medium,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                }
            }
        }

    }
}
