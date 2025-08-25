package com.github.eylulnc.walkmunich.feature.main.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.github.eylulnc.walkmunich.core.ui.CategoryCard
import com.github.eylulnc.walkmunich.feature.main.viewModel.MainScreenViewModel
import com.github.eylulnc.walkmunich.ui.theme.Spacing
import com.github.eylulnc.walkmunich.ui.theme.TypographySizes
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreenUi(
    viewModel: MainScreenViewModel = koinViewModel(),
    onCategoryClick: (Category) -> Unit,
    onFavoritesItemClick: (Long) -> Unit
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
            heroImageKey = state.city?.heroImage?.imageUrl ?: ""
        )

        Spacer(modifier = Modifier.height(Spacing.medium))

        CategoriesSection(
            onCategoryClick = onCategoryClick
        )

    }
}

@Composable
private fun HeaderSection(
    cityName: String,
    heroImageKey: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(Spacing.heroHeight)
    ) {
        Image(
            painter = painterResource(id = R.drawable.hero_munich),
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
    }
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