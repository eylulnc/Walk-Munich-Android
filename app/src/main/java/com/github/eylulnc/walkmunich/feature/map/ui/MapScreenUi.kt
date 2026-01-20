package com.github.eylulnc.walkmunich.feature.map.ui

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.eylulnc.walkmunich.R
import com.github.eylulnc.walkmunich.core.data.model.Category
import com.github.eylulnc.walkmunich.core.data.model.Place
import com.github.eylulnc.walkmunich.core.data.model.toUi
import com.github.eylulnc.walkmunich.core.ui.composable.PlaceCardSmall
import com.github.eylulnc.walkmunich.core.ui.theme.Spacing
import com.github.eylulnc.walkmunich.feature.map.viewmodel.MapViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@SuppressLint("MissingPermission")
@Composable
fun MapScreenUi(
    modifier: Modifier = Modifier,
    viewModel: MapViewModel = koinViewModel(),
    onPlaceClick: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val fusedLocationClient =
        remember { LocationServices.getFusedLocationProviderClient(context) }

    var hasLocationPermission by remember { mutableStateOf(false) }
    var selectedPlace by remember { mutableStateOf<Place?>(null) }
    var showLegendDialog by remember { mutableStateOf(false) }

    val munich = LatLng(48.1351, 11.5820)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(munich, 12f)
    }

    var isMapLoaded by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasLocationPermission = permissions.values.all { it }
    }

    fun moveCamera(target: LatLng, zoom: Float) {
        if (!isMapLoaded) return

        scope.launch {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(target, zoom)
            )
        }
    }


    fun moveToUserLocationOrMunich() {
        if (!hasLocationPermission) {
            moveCamera(munich, 12f)
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    moveCamera(
                        LatLng(location.latitude, location.longitude),
                        16f
                    )
                } else {
                    moveCamera(munich, 12f)
                }
            }
            .addOnFailureListener {
                moveCamera(munich, 12f)
            }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    LaunchedEffect(hasLocationPermission, isMapLoaded) {
        if (isMapLoaded) {
            moveToUserLocationOrMunich()
        }
    }


    Box(modifier = modifier.fillMaxSize()) {

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = hasLocationPermission
            ),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = true,
                compassEnabled = true,
                myLocationButtonEnabled = false
            ),
            onMapLoaded = {
                isMapLoaded = true
            },
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

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Small)
        ) {

            if (hasLocationPermission) {
                Surface(
                    modifier = Modifier.size(Spacing.ActionIconSize),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = Spacing.ExtraSmall
                ) {
                    IconButton(onClick = { moveToUserLocationOrMunich() }) {
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = stringResource(R.string.map_my_location_content_description),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Surface(
                modifier = Modifier.size(Spacing.ActionIconSize),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = Spacing.ExtraSmall
            ) {
                IconButton(onClick = { showLegendDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = stringResource(R.string.map_legend_content_description),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

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
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
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
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.map_legend_close))
            }
        },
        title = {
            Text(
                text = stringResource(R.string.map_legend_categories_title),
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
                                .size(Spacing.ItemGap)
                                .clip(CircleShape)
                                .background(getMarkerColor(category))
                        )
                        Text(
                            text = stringResource(category.toUi().titleResource)
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun rememberMarkerIcon(category: Category): BitmapDescriptor =
    remember(category) {
        BitmapDescriptorFactory.defaultMarker(getMarkerHue(category))
    }

private fun getMarkerHue(category: Category): Float =
    when (category) {
        Category.LANDMARK -> BitmapDescriptorFactory.HUE_RED
        Category.MUSEUM -> BitmapDescriptorFactory.HUE_AZURE
        Category.VIEWPOINT -> BitmapDescriptorFactory.HUE_GREEN
        Category.COFFEE -> BitmapDescriptorFactory.HUE_ORANGE
        Category.FOOD -> BitmapDescriptorFactory.HUE_YELLOW
    }

private fun getMarkerColor(category: Category): Color =
    Color.hsv(
        hue = getMarkerHue(category),
        saturation = 0.8f,
        value = 0.9f
    )
