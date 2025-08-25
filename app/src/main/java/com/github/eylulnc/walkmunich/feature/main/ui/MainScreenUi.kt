package com.github.eylulnc.walkmunich.feature.main.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.eylulnc.walkmunich.R
import com.github.eylulnc.walkmunich.core.data.model.Category
import com.github.eylulnc.walkmunich.core.ui.composable.CategoryCard
import com.github.eylulnc.walkmunich.core.ui.util.ImageResolver
import com.github.eylulnc.walkmunich.feature.main.viewModel.MainScreenViewModel
import com.github.eylulnc.walkmunich.core.ui.theme.OrangeMain
import com.github.eylulnc.walkmunich.core.ui.theme.Spacing
import com.github.eylulnc.walkmunich.core.ui.theme.TypographySizes
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreenUi(
    viewModel: MainScreenViewModel = koinViewModel(),
    onCategoryClick: (Category) -> Unit,
    onPlaceItemClick: (Long) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(
                WindowInsets.navigationBars.only(
                    WindowInsetsSides.Start + WindowInsetsSides.End + WindowInsetsSides.Bottom
                )
            )
            .background(Color.White)
    ) {

        HeaderSection(
            cityName = state.city?.name ?: stringResource(R.string.munich_title),
            heroImageUrl = state.city?.heroImage?.imageUrl ?: "",
            query = state.searchQuery,
            onQueryChange = viewModel::onQueryChange,
            onClearQuery = viewModel::onClearQuery
        )

        Spacer(modifier = Modifier.height(Spacing.medium))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (state.searchQuery.isNotBlank()) {
                SearchResultsSection(
                    searchResults = state.searchResults,
                    onPlaceClick = { place ->
                        onPlaceItemClick(place.id)
                    }
                )
            } else {
                CategoriesSection(
                    onCategoryClick = onCategoryClick
                )
            }
        }
    }
}

@Composable
private fun HeaderSection(
    cityName: String,
    heroImageUrl: String,
    query: String,
    onQueryChange: (String) -> Unit,
    onClearQuery : () -> Unit
) {

    val resId = ImageResolver.resolveDrawable(heroImageUrl)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(Spacing.heroHeight)
    ) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = stringResource(R.string.munich_title),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Text(
            text = cityName,
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontSize = TypographySizes.heroTitle,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(Spacing.large)
        )

        SearchBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = Spacing.SearchBarHorizontal)
                .height(Spacing.SearchBarHeight)
                .offset(y = Spacing.SearchBarOverlap),
            query = query,
            onQueryChange = onQueryChange,
            onClearQuery = onClearQuery
        )
    }

    Spacer(Modifier.height(Spacing.SearchBarOverlap + Spacing.SearchBarHeight / 2))
}


@Composable
private fun CategoriesSection(
    onCategoryClick: (Category) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.medium)
    ) {
        Text(
            text = stringResource(R.string.category_title),
            style = MaterialTheme.typography.headlineSmall,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = Spacing.medium)
        )

        Spacer(modifier = Modifier.height(Spacing.medium))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(Spacing.medium),
            contentPadding = PaddingValues(start = Spacing.medium, end = Spacing.medium)
        ) {
            items(Category.entries.toTypedArray()) { category ->
                CategoryCard(
                    category = category,
                    onClick = { onCategoryClick(category) }
                )
            }
        }
    }
}

@Composable
private fun SearchBar(
    modifier: Modifier = Modifier,
    query: String = "",
    onQueryChange: (String) -> Unit = {},
    onClearQuery: () -> Unit = {}
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = Color.White,
        shadowElevation = 12.dp
    ) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = {
                Text(text = "Search attractions")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = OrangeMain
                )
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear",
                        tint = OrangeMain,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable {
                                onClearQuery()
                            }
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(Spacing.cornerRadius)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true
        )
    }

}