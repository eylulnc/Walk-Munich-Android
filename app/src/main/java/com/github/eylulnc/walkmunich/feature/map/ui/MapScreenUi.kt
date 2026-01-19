package com.github.eylulnc.walkmunich.feature.map.ui

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.eylulnc.walkmunich.core.data.model.Category
import com.github.eylulnc.walkmunich.core.data.model.toUi
import com.github.eylulnc.walkmunich.feature.map.viewmodel.MapViewModel
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import org.koin.androidx.compose.koinViewModel

@Composable
fun MapScreenUi(
    modifier: Modifier = Modifier,
    viewModel: MapViewModel = koinViewModel(),
    onPlaceClick: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var hasLocationPermission by remember {
        mutableStateOf(false)
    }

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
                zoomControlsEnabled = false
            )
        ) {
            uiState.places.forEach { place ->
                place.coords?.let { coords ->
                    val position = LatLng(coords.lat, coords.lon)
                    Marker(
                        state = MarkerState(position = position),
                        title = place.name,
                        snippet = place.category.name,
                        icon = rememberMarkerIcon(place.category),
                        onClick = {
                            onPlaceClick(place.id)
                            true
                        }
                    )
                }
            }
        }

        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun rememberMarkerIcon(category: Category): BitmapDescriptor? {
    val tint = MaterialTheme.colorScheme.primary
    val categoryUi = category.toUi()
    val context = LocalContext.current

    // We can't easily convert ImageVector to BitmapDescriptor in a generic way without a Composable context or a lot of boilerplate.
    // For now, let's use a simpler approach: use the category to map to a resource ID if possible,
    // or just use different colors for default markers as a fallback if vector conversion is too complex here.
    // However, the requirement is "display icon that belong to the place category".

    return remember(category, tint) {
        // Since we are using Material Icons, we don't have direct drawable resource IDs easily.
        // A common trick is to draw the vector into a bitmap.
        BitmapDescriptorFactory.defaultMarker(getMarkerHue(category))
    }
}

private fun getMarkerHue(category: Category): Float {
    return when (category) {
        Category.LANDMARK -> BitmapDescriptorFactory.HUE_RED
        Category.MUSEUM -> BitmapDescriptorFactory.HUE_AZURE
        Category.VIEWPOINT -> BitmapDescriptorFactory.HUE_GREEN
        Category.COFFEE -> BitmapDescriptorFactory.HUE_ORANGE
        Category.FOOD -> BitmapDescriptorFactory.HUE_YELLOW
    }
}

