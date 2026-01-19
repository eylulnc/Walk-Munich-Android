package com.github.eylulnc.walkmunich.feature.map.ui

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.eylulnc.walkmunich.core.data.model.Category
import com.github.eylulnc.walkmunich.core.data.model.Place
import com.github.eylulnc.walkmunich.core.data.model.toUi
import com.github.eylulnc.walkmunich.core.ui.composable.PlaceCardSmall
import com.github.eylulnc.walkmunich.core.ui.theme.Spacing
import com.github.eylulnc.walkmunich.feature.map.viewmodel.MapViewModel
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import org.koin.androidx.compose.koinViewModel

@Composable
fun MapScreenUi(
    modifier: Modifier = Modifier,
    viewModel: MapViewModel = koinViewModel(),
    onPlaceClick: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var hasLocationPermission by remember { mutableStateOf(false) }
    var selectedPlace by remember { mutableStateOf<Place?>(null) }
    var showLegendDialog by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasLocationPermission = permissions.values.all { it }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    val munich = LatLng(48.1351, 11.5820)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(munich, 12f)
    }

    Box(modifier = modifier.fillMaxSize()) {

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = hasLocationPermission
            ),
            uiSettings = MapUiSettings(
                myLocationButtonEnabled = hasLocationPermission,
                zoomControlsEnabled = true,
                compassEnabled = true
            ),
            onMapClick = { selectedPlace = null }
        ) {
            uiState.places.forEach { place ->
                place.coords?.let { coords ->
                    Marker(
                        state = MarkerState(
                            position = LatLng(coords.lat, coords.lon)
                        ),
                        title = place.name,
                        icon = rememberMarkerIcon(place.category),
                        onClick = {
                            selectedPlace = place
                            false
                        }
                    )
                }
            }
        }

        // ⓘ Info button (bottom-left)
        Surface(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(Spacing.Medium)
                .size(48.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp,
            shadowElevation = 4.dp
        ) {
            IconButton(onClick = { showLegendDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Map legend",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Selected place card
        AnimatedVisibility(
            visible = selectedPlace != null,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(Spacing.Medium)
        ) {
            selectedPlace?.let { place ->
                PlaceCardSmall(
                    place = place,
                    onPlaceClick = { onPlaceClick(place.id) }
                )
            }
        }

        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }

    if (showLegendDialog) {
        CategoryLegendDialog(
            onDismiss = { showLegendDialog = false }
        )
    }
}

@Composable
fun CategoryLegendDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        },
        title = {
            Text(
                text = "Categories",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.Small)) {
                Category.entries.forEach { category ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.Small)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(15.dp)
                                .clip(CircleShape)
                                .background(getMarkerColor(category))
                        )
                        Text(
                            text = stringResource(category.toUi().titleResource),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun rememberMarkerIcon(category: Category): BitmapDescriptor {
    return remember(category) {
        BitmapDescriptorFactory.defaultMarker(getMarkerHue(category))
    }
}

private fun getMarkerHue(category: Category): Float =
    when (category) {
        Category.LANDMARK -> BitmapDescriptorFactory.HUE_RED
        Category.MUSEUM -> BitmapDescriptorFactory.HUE_AZURE
        Category.VIEWPOINT -> BitmapDescriptorFactory.HUE_GREEN
        Category.COFFEE -> BitmapDescriptorFactory.HUE_ORANGE
        Category.FOOD -> BitmapDescriptorFactory.HUE_YELLOW
    }

private fun getMarkerColor(category: Category): Color {
    val hue = getMarkerHue(category)
    return Color.hsv(
        hue = hue,
        saturation = 1f,
        value = 1f
    )
}

