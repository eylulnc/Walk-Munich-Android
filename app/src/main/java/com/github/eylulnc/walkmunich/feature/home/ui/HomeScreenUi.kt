package com.github.eylulnc.walkmunich.feature.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.eylulnc.walkmunich.core.data.model.Category
import com.github.eylulnc.walkmunich.core.data.model.Place
import com.github.eylulnc.walkmunich.core.ui.composable.WMTopAppBarScreen
import com.github.eylulnc.walkmunich.core.ui.util.ImageResolver
import com.github.eylulnc.walkmunich.feature.home.viewModel.HomeScreenViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenUi(
    viewModel: HomeScreenViewModel = koinViewModel(),
    onPlaceItemClick: (Long) -> Unit,
    onSeeAllFavoritesClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        WMTopAppBarScreen(title = "Walk Munich") {
            FilteredPlacesSection(
                places = state.filteredPlaces,
                onPlaceClick = onPlaceItemClick,
                selectedCategory = state.selectedCategory,
                onCategorySelected = viewModel::onCategorySelected
            )
            Spacer(modifier = Modifier.height(24.dp))
            state.highlightedPlace?.let { HighlightSection(it) }
            Spacer(modifier = Modifier.height(24.dp))
            FavoritesSection(
                onSeeAllClick = onSeeAllFavoritesClick,
                state.filteredPlaces,
                onPlaceItemClick
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun CategoryChips(
    selectedCategory: Category?,
    onCategorySelected: (Category?) -> Unit
) {
    val categories = remember { listOf<Category?>(null) + Category.values() }
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            val isSelected = selectedCategory == category
            Button(
                onClick = { onCategorySelected(category) },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                val text = when (category) {
                    null -> "For You"
                    else -> category.name.lowercase().replaceFirstChar { it.titlecase() }
                }
                Text(text = text, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun FilteredPlacesSection(
    places: List<Place>,
    onPlaceClick: (Long) -> Unit,
    selectedCategory: Category?,
    onCategorySelected: (Category?) -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Places", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            TextButton(onClick = { /*TODO*/ }) {
                Text("See All", color = MaterialTheme.colorScheme.primary)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        CategoryChips(
            selectedCategory = selectedCategory,
            onCategorySelected = onCategorySelected
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(places.take(5)) { place ->
                PlaceCard(place = place, onPlaceClick = { onPlaceClick(place.id) })
            }
        }
    }
}

@Composable
fun FavoritesSection(
    onSeeAllClick: () -> Unit,
    favorites: List<Place>,
    onPlaceClick: (Long) -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Your Favorites", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            TextButton(onClick = onSeeAllClick) {
                Text("See All", color = MaterialTheme.colorScheme.primary)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(favorites.take(5)) { place ->
                PlaceCard(place = place, onPlaceClick = { onPlaceClick(place.id) })
            }
        }
    }
}

@Composable
fun HighlightSection(place: Place) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(text = "Highlight of the Day", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        HighlightCard(place = place)
    }
}


@Composable
fun PlaceCard(
    place: Place,
    onPlaceClick: () -> Unit,
    isFavorite: Boolean = false
) {
    Card(
        modifier = Modifier.width(256.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        val imageResId = ImageResolver.resolveDrawable(place.imageUrl)

        Box(modifier = Modifier
            .height(150.dp)
            .clickable(onClick = onPlaceClick)) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = "Place Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.TopEnd)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.3f))
                    .padding(4.dp)
            ) {
                Icon(
                    if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = Color.White
                )
            }
        }
        Column(modifier = Modifier.padding(8.dp)) {
            Text(place.name, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun HighlightCard(place: Place) {
    val imageResId = ImageResolver.resolveDrawable(place.imageUrl)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.height(200.dp)) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = "Highlight Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Color.Black.copy(alpha = 0.4f)
                    )
            )
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.TopEnd)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.3f))
                    .padding(4.dp)
            ) {
                Icon(
                    Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = Color.White
                )
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    place.name,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HomeScreenUi(
        onPlaceItemClick = {},
        onSeeAllFavoritesClick = {}
    )
}
