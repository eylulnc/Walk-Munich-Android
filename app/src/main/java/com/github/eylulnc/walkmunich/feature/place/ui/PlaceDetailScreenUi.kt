package com.github.eylulnc.walkmunich.feature.place.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.eylulnc.walkmunich.R
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
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
        ) {
            val imageId = ImageResolver.resolveDrawable(place.imageUrl)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Spacing.HeroHeight)
            ) {
                Image(
                    painter = painterResource(imageId),
                    contentDescription = place.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            StoryContent(place = place, subTitle = subTitle)
        }

        TopAppBar(
            title = { /* no title here */ },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                        tint = OrangeMain
                    )
                }
            },
            actions = {
                IconButton(onClick = { /* TODO: Favorite toggle */ }) {
                    Icon(
                        imageVector = Icons.Filled.FavoriteBorder,
                        contentDescription = stringResource(R.string.favorite),
                        tint = OrangeMain
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                titleContentColor = OrangeMain,
                scrolledContainerColor = Color.Transparent,
                navigationIconContentColor = OrangeMain,
                actionIconContentColor = OrangeMain
            ),
            scrollBehavior = scrollBehavior,
            modifier = Modifier.statusBarsPadding()
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
                .offset(y = Spacing.NegativeCardOffset)
                .clip(RoundedCornerShape(topStart = Spacing.CardCornerRadius, topEnd = Spacing.CardCornerRadius))
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = Spacing.Large, vertical = Spacing.Large)
        ) {
            Column {
                Text(
                    text = place.name,
                    fontSize = TypographySizes.large,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                )

                if (!subTitle.isNullOrEmpty()) {
                    Text(
                        text = subTitle,
                        fontSize = TypographySizes.medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(Modifier.height(Spacing.Small))

                Text(
                    text = story.overview,
                    fontSize = TypographySizes.medium,
                    color = MaterialTheme.colorScheme.onBackground,
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
        text = stringResource(R.string.highlights),
        fontSize = TypographySizes.subtitle,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
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
        text = stringResource(R.string.fun_facts),
        fontSize = TypographySizes.subtitle,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
    )
    Spacer(modifier = Modifier.height(Spacing.Small))
    facts.forEach { fact ->
        Text(
            text = stringResource(R.string.bullet_fact, fact.text),
            fontSize = TypographySizes.medium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = Spacing.ExtraSmall)
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
                    color = MaterialTheme.colorScheme.onBackground
                )
            ) { append(highlight.title) }

            append(": ")

            withStyle(
                style = androidx.compose.ui.text.SpanStyle(
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onBackground
                )
            ) { append(highlight.text) }
        },
        fontSize = TypographySizes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.ExtraSmall)
    )
}
