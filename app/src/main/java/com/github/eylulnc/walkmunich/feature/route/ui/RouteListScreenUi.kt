package com.github.eylulnc.walkmunich.feature.route.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.eylulnc.walkmunich.core.data.model.RouteSummary
import com.github.eylulnc.walkmunich.core.ui.theme.Spacing
import com.github.eylulnc.walkmunich.feature.route.viewmodel.RouteListViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun RouteListScreenUi(
    viewModel: RouteListViewModel = koinViewModel(),
    onRouteClick: (Long) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .safeContentPadding()
    ) {
        when {
            state.isLoading -> {
                // Center the loading indicator
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            state.error != null -> {
                // Center the error message
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.error ?: "Unknown error",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            else -> {
                LazyColumn {
                    items(state.routes) { route ->
                        RouteRow(route = route) { onRouteClick(route.id) }
                        Spacer(Modifier.height(Spacing.Medium))
                    }
                }
            }
        }
    }
}

@Composable
private fun RouteRow(route: RouteSummary, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(
            defaultElevation = Spacing.ExtraSmall
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(
            width = Spacing.BorderStroke,
            color = Color.LightGray
        ),
        shape = RoundedCornerShape(Spacing.CornerRadius),
    ) {
        Column(modifier = Modifier.padding(Spacing.Medium)) {
            Text(
                text = route.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            if (!route.summary.isNullOrBlank()) {
                Spacer(Modifier.height(Spacing.Small))
                Text(text = route.summary, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}


