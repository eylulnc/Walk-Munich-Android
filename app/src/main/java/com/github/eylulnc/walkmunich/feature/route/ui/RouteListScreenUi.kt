package com.github.eylulnc.walkmunich.feature.route.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.eylulnc.walkmunich.R
import com.github.eylulnc.walkmunich.core.data.model.RouteSummary
import com.github.eylulnc.walkmunich.core.ui.composable.ErrorState
import com.github.eylulnc.walkmunich.core.ui.composable.LoadingState
import com.github.eylulnc.walkmunich.core.ui.theme.Spacing
import com.github.eylulnc.walkmunich.core.ui.theme.TypographySizes
import com.github.eylulnc.walkmunich.core.ui.util.ImageResolver
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
            .background(MaterialTheme.colorScheme.background)
            .safeContentPadding()
    ) {
        when {
            state.isLoading -> {
                LoadingState()
            }

            state.error != null -> {
                ErrorState(errorMessage = state.error)
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
private fun RouteRow(
    route: RouteSummary,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(Spacing.CardHeightMedium)
            .clickable { onClick() },
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
        Column(modifier = Modifier.fillMaxWidth()) {
            val imageResId =
                route.imageUrl?.let { ImageResolver.resolveDrawable(it) } ?: R.drawable.hero_munich

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Spacing.CardHeightSmall)
                    .weight(0.4f)
            ) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = route.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // 📝 Text Section
            Column(
                modifier = Modifier
                    .weight(0.6f)
                    .padding(horizontal = Spacing.Medium, vertical = Spacing.Small),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = route.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = TypographySizes.medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (!route.summary.isNullOrBlank()) {
                    Text(
                        text = route.summary,
                        fontSize = TypographySizes.small,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = Spacing.Small)
                    )
                }
            }
        }
    }
}
