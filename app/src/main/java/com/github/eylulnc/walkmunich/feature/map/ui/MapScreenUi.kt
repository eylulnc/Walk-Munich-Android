package com.github.eylulnc.walkmunich.feature.map.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.eylulnc.walkmunich.feature.map.viewmodel.MapViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MapScreenUi(
    modifier: Modifier = Modifier,
    viewModel: MapViewModel = koinViewModel(),
    onPlaceClick: (Long) -> Unit
) {

}
