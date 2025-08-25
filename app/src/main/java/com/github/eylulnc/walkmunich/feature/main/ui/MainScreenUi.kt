package com.github.eylulnc.walkmunich.feature.main.ui

import androidx.compose.runtime.Composable
import com.github.eylulnc.walkmunich.feature.main.viewModel.MainScreenViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreenUi(
    viewModel: MainScreenViewModel = koinViewModel(),
    onCategoryClick: (Long) -> Unit,
    onFavoritesItemClick: (Long) -> Unit) {
}