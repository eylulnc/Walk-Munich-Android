package com.github.eylulnc.walkmunich.feature.place.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.eylulnc.walkmunich.core.data.model.Fact
import com.github.eylulnc.walkmunich.core.data.model.Highlight
import com.github.eylulnc.walkmunich.core.data.model.Place
import com.github.eylulnc.walkmunich.core.ui.composable.ErrorState
import com.github.eylulnc.walkmunich.core.ui.composable.LoadingState
import com.github.eylulnc.walkmunich.core.ui.theme.OrangeMain
import com.github.eylulnc.walkmunich.core.ui.theme.Spacing
import com.github.eylulnc.walkmunich.core.ui.theme.TypographySizes
import com.github.eylulnc.walkmunich.core.ui.util.ImageResolver
import com.github.eylulnc.walkmunich.feature.place.viewmodel.PlaceViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun PlaceDetailScreenUi(
    viewModel: PlaceViewModel = koinViewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    when {
        state.isLoading -> LoadingState()
        state.error != null -> ErrorState(errorMessage = state.error)
        state.place != null -> PlaceDetailContent(
            place = state.place!!,
            subTitle = state.subTitle,
            onBackClick = onBackClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlaceDetailContent(
    place: Place,
    subTitle: String?,
    onBackClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // --- Scrollable content ---
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .background(Color.White)
        ) {
            // Hero image
            val imageId = ImageResolver.resolveDrawable(place.imageUrl)
            Image(
                painter = painterResource(imageId),
                contentDescription = place.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )

            // Content below image
            StoryContent(place = place, subTitle = subTitle)
        }

        // --- Overlayed TopAppBar ---
        TopAppBar(
            title = { },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            actions = {
                IconButton(onClick = { /* TODO: Favorite toggle */ }) {
                    Icon(
                        imageVector = Icons.Filled.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White
            ),
            modifier = Modifier
                .background(
                    // Optional gradient overlay for better readability
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.4f),
                            Color.Transparent
                        )
                    )
                )
                .statusBarsPadding()
        )
    }
}

@Composable
private fun StoryContent(
    place: Place,
    subTitle: String?
) {
    place.story?.let { story ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-28).dp)
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(Color.White)
                .padding(horizontal = Spacing.Large, vertical = Spacing.Large)
        ) {
            Column {
                Text(
                    text = place.name,
                    fontSize = TypographySizes.large,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray,
                )

                if (!subTitle.isNullOrEmpty()) {
                    Text(
                        text = subTitle,
                        fontSize = TypographySizes.medium,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = Spacing.Small)
                    )
                }

                Text(
                    text = story.overview,
                    fontSize = TypographySizes.medium,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Start
                )

                if (story.highlights.isNotEmpty()) {
                    HighlightsSection(highlights = story.highlights)
                }

                if (!place.facts.isNullOrEmpty()) {
                    FactsSection(facts = place.facts)
                }
            }
        }
    }
}

@Composable
private fun HighlightsSection(highlights: List<Highlight>) {
    Spacer(modifier = Modifier.height(Spacing.Large))
    Text(
        text = "Highlights",
        fontSize = TypographySizes.medium,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )
    Spacer(modifier = Modifier.height(Spacing.Small))
    highlights.forEach { highlight ->
        HighlightItem(highlight)
    }
}

@Composable
private fun FactsSection(facts: List<Fact>) {
    Spacer(modifier = Modifier.height(Spacing.Large))
    Text(
        text = "Fun Facts",
        fontSize = TypographySizes.medium,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )
    Spacer(modifier = Modifier.height(Spacing.Small))
    facts.forEach { fact ->
        Text(
            text = "• ${fact.text}",
            fontSize = TypographySizes.medium,
            color = Color.DarkGray,
            modifier = Modifier.padding(bottom = 4.dp)
        )
    }
}

@Composable
private fun HighlightItem(highlight: Highlight) {
    Text(
        buildAnnotatedString {
            withStyle(
                style = androidx.compose.ui.text.SpanStyle(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            ) { append(highlight.title) }

            append(": ")

            withStyle(
                style = androidx.compose.ui.text.SpanStyle(
                    fontWeight = FontWeight.Normal,
                    color = Color.DarkGray
                )
            ) { append(highlight.text) }
        },
        fontSize = TypographySizes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.ExtraSmall)
    )
}
