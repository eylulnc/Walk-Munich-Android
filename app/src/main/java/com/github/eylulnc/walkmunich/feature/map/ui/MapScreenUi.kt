package com.github.eylulnc.walkmunich.feature.map.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.location.LocationManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Attractions
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Museum
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.NaturePeople
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.createBitmap
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.eylulnc.walkmunich.R
import com.github.eylulnc.walkmunich.core.data.model.Category
import com.github.eylulnc.walkmunich.core.ui.theme.BlueTeal
import com.github.eylulnc.walkmunich.core.ui.theme.Green
import com.github.eylulnc.walkmunich.core.ui.theme.OrangeMain
import com.github.eylulnc.walkmunich.core.ui.theme.RedError
import com.github.eylulnc.walkmunich.core.ui.theme.Spacing
import com.github.eylulnc.walkmunich.core.ui.theme.Yellow
import com.github.eylulnc.walkmunich.feature.map.viewmodel.MapViewModel
import com.github.eylulnc.walkmunich.feature.map.viewmodel.munich
import org.koin.androidx.compose.koinViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker

@SuppressLint("MissingPermission")
@Composable
fun MapScreenUi(
    modifier: Modifier = Modifier,
    viewModel: MapViewModel = koinViewModel(),
    onPlaceClick: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val snackbarHostState = remember { SnackbarHostState() }

    var hasLocationPermission by remember { mutableStateOf(false) }
    val selectedPlace = uiState.selectedPlace

    val markerBitmaps = rememberMarkerBitmaps()
    val currentMarkerBitmaps by rememberUpdatedState(markerBitmaps)

    val mapView = remember {
        Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", 0))
        Configuration.getInstance().userAgentValue = context.packageName
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            isHorizontalMapRepetitionEnabled = false
            isVerticalMapRepetitionEnabled = false
            controller.setZoom(uiState.savedCameraZoom)
            controller.setCenter(GeoPoint(uiState.savedCameraLat, uiState.savedCameraLon))
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasLocationPermission = permissions.values.all { it }
    }

    // Add/update place markers whenever places load
    LaunchedEffect(uiState.places) {
        if (uiState.places.isEmpty()) return@LaunchedEffect
        val bitmaps = currentMarkerBitmaps
        mapView.overlays.removeAll { it is Marker }
        uiState.places.forEach { place ->
            place.coords?.let { coords ->
                val marker = Marker(mapView).apply {
                    position = GeoPoint(coords.lat, coords.lon)
                    title = place.name
                    icon = bitmaps[place.category]?.toDrawable(context.resources)
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                    setOnMarkerClickListener { _, _ ->
                        viewModel.selectPlace(place)
                        true
                    }
                }
                mapView.overlays.add(marker)
            }
        }
        mapView.invalidate()
    }

    // Map tap (deselect) + camera tracking
    DisposableEffect(mapView) {
        val eventsOverlay = MapEventsOverlay(object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                viewModel.clearSelectedPlace()
                return true
            }
            override fun longPressHelper(p: GeoPoint): Boolean = false
        })
        mapView.overlays.add(0, eventsOverlay)

        val cameraListener = object : MapListener {
            override fun onScroll(event: ScrollEvent): Boolean {
                viewModel.saveCameraPosition(
                    mapView.mapCenter.latitude,
                    mapView.mapCenter.longitude,
                    mapView.zoomLevelDouble
                )
                return false
            }
            override fun onZoom(event: ZoomEvent): Boolean {
                viewModel.saveCameraPosition(
                    mapView.mapCenter.latitude,
                    mapView.mapCenter.longitude,
                    mapView.zoomLevelDouble
                )
                return false
            }
        }
        mapView.addMapListener(cameraListener)

        onDispose {
            mapView.removeMapListener(cameraListener)
            mapView.onDetach()
        }
    }

    // Pause/resume with lifecycle
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    // Centre on user only on first launch
    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission && !uiState.hasCenteredCamera) {
            val location = getLastKnownLocation(context)
            val target = if (location != null) GeoPoint(location.latitude, location.longitude)
                         else GeoPoint(munich.lat, munich.lon)
            val zoom = if (location != null) 18.5 else 12.0
            mapView.controller.animateTo(target, zoom, 1000L)
            viewModel.markCameraCentered()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { snackbarHostState.showSnackbar(it) }
    }

    Box(modifier = modifier.fillMaxSize()) {

        AndroidView(
            factory = { mapView },
            modifier = Modifier.fillMaxSize()
        )

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
                    IconButton(onClick = {
                        val location = getLastKnownLocation(context)
                        val target = if (location != null) GeoPoint(location.latitude, location.longitude)
                                     else GeoPoint(munich.lat, munich.lon)
                        mapView.controller.animateTo(target, 18.5, 1000L)
                    }) {
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = stringResource(R.string.map_my_location_content_description),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
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
                .padding(bottom = Spacing.ExtraLarge)
        ) {
            selectedPlace?.let { place ->
                MapPlaceCard(
                    place = place,
                    onPlaceClick = { onPlaceClick(place.id) }
                )
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

// MARK: - Marker bitmaps (composable — uses VectorPainter for icons)

@Composable
private fun rememberMarkerBitmaps(): Map<Category, Bitmap> {
    val density = LocalDensity.current
    val landmark  = rememberSingleMarkerBitmap(Category.LANDMARK, density)
    val museum    = rememberSingleMarkerBitmap(Category.MUSEUM, density)
    val viewpoint = rememberSingleMarkerBitmap(Category.VIEWPOINT, density)
    val coffee    = rememberSingleMarkerBitmap(Category.COFFEE, density)
    val food      = rememberSingleMarkerBitmap(Category.FOOD, density)
    return remember(landmark, museum, viewpoint, coffee, food) {
        mapOf(
            Category.LANDMARK  to landmark,
            Category.MUSEUM    to museum,
            Category.VIEWPOINT to viewpoint,
            Category.COFFEE    to coffee,
            Category.FOOD      to food
        )
    }
}

@Composable
private fun rememberSingleMarkerBitmap(category: Category, density: Density): Bitmap {
    val color = category.markerColor()
    val painter = rememberVectorPainter(image = category.markerIcon())
    return remember(category) {
        val sizePx = with(density) { 36.dp.roundToPx() }
        val bitmap = createBitmap(sizePx, sizePx)
        val composeCanvas = androidx.compose.ui.graphics.Canvas(
            android.graphics.Canvas(bitmap)
        )
        CanvasDrawScope().draw(
            density = density,
            layoutDirection = LayoutDirection.Ltr,
            canvas = composeCanvas,
            size = Size(sizePx.toFloat(), sizePx.toFloat())
        ) {
            drawCircle(color = color)
            val iconSize = size.width * 0.56f
            val offset = (size.width - iconSize) / 2f
            translate(left = offset, top = offset) {
                with(painter) {
                    draw(Size(iconSize, iconSize), colorFilter = ColorFilter.tint(Color.White))
                }
            }
        }
        bitmap
    }
}

private fun Category.markerColor(): Color = when (this) {
    Category.LANDMARK  -> RedError
    Category.MUSEUM    -> BlueTeal
    Category.VIEWPOINT -> Green
    Category.COFFEE    -> OrangeMain
    Category.FOOD      -> Yellow
}

private fun Category.markerIcon(): ImageVector = when (this) {
    Category.LANDMARK  -> Icons.Filled.Attractions
    Category.MUSEUM    -> Icons.Filled.Museum
    Category.VIEWPOINT -> Icons.Filled.NaturePeople
    Category.COFFEE    -> Icons.Filled.LocalCafe
    Category.FOOD      -> Icons.Filled.Restaurant
}

// MARK: - Location helper

@SuppressLint("MissingPermission")
private fun getLastKnownLocation(context: Context): android.location.Location? {
    val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        ?: lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        ?: lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
}
