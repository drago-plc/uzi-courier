package com.lomolo.uzicourier.compose.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.CustomCap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.PolyUtil
import com.google.maps.android.SphericalUtil
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.lomolo.uzicourier.DeviceDetails
import com.lomolo.uzicourier.MainViewModel
import com.lomolo.uzicourier.R
import com.lomolo.uzicourier.compose.loader.Loader
import com.lomolo.uzicourier.compose.onboarding.OnboardingDestination
import com.lomolo.uzicourier.compose.signin.SessionViewModel
import com.lomolo.uzicourier.compose.signin.UserNameDestination
import com.lomolo.uzicourier.model.CourierStatus
import com.lomolo.uzicourier.model.Session
import com.lomolo.uzicourier.model.Trip

@Composable
internal fun HomeSuccessScreen(
    modifier: Modifier = Modifier,
    tripViewModel: TripViewModel,
    mainViewModel: MainViewModel,
    sessionViewModel: SessionViewModel,
    snackbarHostState: SnackbarHostState,
    deviceDetails: DeviceDetails,
    onGetStartedClick: () -> Unit,
    session: Session,
    onNavigateTo: (String) -> Unit = {},
    assignment: Trip
) {
    val isAuthed = session.token.isNotBlank()
    val isOnboarding = session.onboarding
    val courierStatus = session.courierStatus

    when {
        isAuthed && isOnboarding -> {
            onNavigateTo(UserNameDestination.route)
        }
        isAuthed && courierStatus == CourierStatus.ONBOARDING -> {
            onNavigateTo(OnboardingDestination.route)
        }
        else -> {
            DefaultHomeScreen(
                modifier = modifier,
                mainViewModel = mainViewModel,
                sessionViewModel = sessionViewModel,
                snackbarHostState = snackbarHostState,
                deviceDetails = deviceDetails,
                onGetStartedClick = onGetStartedClick,
                session = session,
                assignment = assignment,
                tripViewModel = tripViewModel
            )
        }
    }
}

@Composable
private fun DefaultHomeScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
    tripViewModel: TripViewModel,
    snackbarHostState: SnackbarHostState,
    sessionViewModel: SessionViewModel,
    deviceDetails: DeviceDetails,
    onGetStartedClick: () -> Unit,
    session: Session,
    assignment: Trip
) {
    val context = LocalContext.current
    val hasAssignment = assignment.id.isNotBlank()
    val isAuthed = session.token.isNotBlank()
    val uiSettings by remember {
        mutableStateOf(MapUiSettings(zoomControlsEnabled = false, compassEnabled = false))
    }
    val mapProperties by remember {
        mutableStateOf(
            MapProperties(
                mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
                    context,
                    R.raw.style_json
                )
            )
        )
    }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(deviceDetails.gps, 17f)
    }
    var polyline by rememberSaveable {
        mutableStateOf(listOf<LatLng>())
    }
    val courierPosition = rememberMarkerState(
        position = deviceDetails.gps
    )

    LaunchedEffect(Unit) { tripViewModel.getCourierAssignedTrip() }
    if (isAuthed) tripViewModel.getTripAssignment(session.id).collectAsState(initial = null)

    GoogleMap(
        modifier = modifier,
        properties = mapProperties,
        onMapLoaded = { mainViewModel.setMapLoaded(true) },
        uiSettings = uiSettings,
        cameraPositionState = cameraPositionState
    ) {
        Polyline(
            width = 12f,
            points = polyline,
            geodesic = true,
            endCap = CustomCap(BitmapDescriptorFactory.fromResource(R.drawable.icons8_filled_circle_30)),
        )
        if (polyline.isNotEmpty()) {
            Marker(
                zIndex = 1.0f,
                anchor = Offset(0.5f, 0.5f),
                state = MarkerState(deviceDetails.gps),
                icon = BitmapDescriptorFactory.fromResource(R.drawable.icons8_navigation_90___),
                flat = true,
            )
        }
    }
    AnimatedVisibility(
        visible = deviceDetails.mapLoaded,
        modifier = modifier,
        exit = fadeOut(),
        enter = EnterTransition.None
    ) {
        Box {
            when {
                !isAuthed -> {
                    NotAuthedScreen(
                        modifier = Modifier
                            .align(Alignment.BottomCenter),
                        onGetStartedClick = onGetStartedClick
                    )
                }
                isAuthed && !hasAssignment -> {
                    ReadyToWork(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(28.dp),
                        mainViewModel = mainViewModel,
                        sessionViewModel = sessionViewModel,
                        snackbarHostState = snackbarHostState,
                        session = session
                    )
                }
                else -> {
                    when (val s = tripViewModel.getCourierTripState) {
                        is GetCourierTripState.Success -> {
                            if (s.trip != null) {
                                TripScreen(
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter),
                                    tripViewModel = tripViewModel,
                                    trip = s.trip,
                                    assignment = assignment
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
