
package com.github.eylulnc.walkmunich.feature.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.github.eylulnc.walkmunich.R
import com.github.eylulnc.walkmunich.core.data.model.Category
import com.github.eylulnc.walkmunich.core.ui.composable.WMTopAppBarScreen
import com.github.eylulnc.walkmunich.feature.home.viewModel.HomeScreenViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenUi(
    viewModel: HomeScreenViewModel = koinViewModel(),
    onCategoryClick: (Category) -> Unit,
    onPlaceItemClick: (Long) -> Unit,
    onSettingsClick: () -> Unit,
    onSeeAllFavoritesClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        WMTopAppBarScreen(title = "Walk Munich") {
            CategoryChips()
            Spacer(modifier = Modifier.height(24.dp))
            FavoritesSection(onSeeAllClick = onSeeAllFavoritesClick)
            Spacer(modifier = Modifier.height(24.dp))
            HighlightSection()
            Spacer(modifier = Modifier.height(24.dp))
            AllToursSection()
        }

    }
}

@Composable
fun CategoryChips() {
    val categories = listOf("For You", "Popular", "Art & Culture", "History", "Beer Gardens")
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            Button(
                onClick = { /* TODO */ },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (category == "For You") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (category == "For You") Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Text(text = category, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun FavoritesSection(onSeeAllClick: () -> Unit) {
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
            items(2) {
                PlaceCard(isFavorite = true)
            }
        }
    }
}

@Composable
fun HighlightSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(text = "Highlight of the Day", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        HighlightCard()
    }
}

@Composable
fun AllToursSection() {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "All Tours", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            TextButton(onClick = { /*TODO*/ }) {
                Text("See All", color = MaterialTheme.colorScheme.primary)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(3) {
                PlaceCard(isFavorite = it % 2 == 0, showDetails = true)
            }
        }
    }
}

@Composable
fun PlaceCard(isFavorite: Boolean, showDetails: Boolean = false) {
    Card(
        modifier = Modifier.width(256.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.height(150.dp)) {
            Image(
                painter = painterResource(id = R.drawable.placeholder), // Replace with actual image
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
            Text("Place Name", fontWeight = FontWeight.Bold)
//            if (showDetails) {
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Text("🕒 2hr", fontSize = 12.sp)
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text("€15", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
//                }
//            } else {
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Icon(Icons.Default.Star, contentDescription = "Rating", tint = Color(0xFFFFD700), modifier = Modifier.size(16.dp))
//                    Text("4.9 (302)", fontSize = 12.sp)
//                }
//            }
        }
    }
}

@Composable
fun HighlightCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.height(200.dp)) {
            Image(
                painter = painterResource(id = R.drawable.placeholder), // Replace with actual image
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
                Text("Eisbach River Surfers", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 18.sp)
                Text("Watch the famous river surfers ride the wave.", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HomeScreenUi(
        onCategoryClick = {},
        onPlaceItemClick = {},
        onSettingsClick = {},
        onSeeAllFavoritesClick = {}
    )
}
