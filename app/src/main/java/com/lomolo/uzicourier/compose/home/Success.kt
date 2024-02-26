package com.lomolo.uzicourier.compose.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.CustomCap
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import com.google.maps.android.SphericalUtil
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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
    LaunchedEffect(Unit) {
        tripViewModel.getTripAssignment(session.id)
    }
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
    val hasAssignment = assignment.id.isNotBlank()
    val isAuthed = session.token.isNotBlank()
    val uiSettings by remember {
        mutableStateOf(MapUiSettings(zoomControlsEnabled = false, compassEnabled = false))
    }
    val mapProperties by remember {
        mutableStateOf(MapProperties(mapType = MapType.TERRAIN))
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
    LaunchedEffect(key1 = tripViewModel.getCourierTripState, key3=polyline, key2 = deviceDetails.gps) {
        when(val s = tripViewModel.getCourierTripState) {
            is GetCourierTripState.Success -> {
                if (s.trip != null) {
                    courierPosition.position = deviceDetails.gps
                    if (s.trip.end_location != null && deviceDetails.mapLoaded) {
                        cameraPositionState.animate(
                            CameraUpdateFactory.newCameraPosition(
                                CameraPosition(
                                    if (polyline.isNotEmpty()) polyline.first() else deviceDetails.gps,
                                    17f,
                                    60f,
                                    if (polyline.isNotEmpty()) SphericalUtil.computeHeading(
                                        deviceDetails.gps,
                                        polyline.last()
                                    ).toFloat() else 0f
                                )
                            )
                        )
                    }
                    if (s.trip.route != null) polyline = PolyUtil.decode(s.trip.route.polyline)
                    if (polyline.isNotEmpty()) {
                        tripViewModel.reverseGeocode(polyline.last())
                        if (PolyUtil.isLocationOnPath(deviceDetails.gps, polyline, true)) {
                            val newRoute = polyline.subList(
                                0,
                                PolyUtil.locationIndexOnPath(
                                    deviceDetails.gps,
                                    polyline.toMutableList(),
                                    true
                                )+1
                            ).toMutableList()
                            newRoute.add(deviceDetails.gps)
                            polyline = newRoute.toList()
                        }
                    }
                }
            }
        }
    }

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
                state = MarkerState(polyline.first()),
                icon = BitmapDescriptorFactory.fromResource(R.drawable.icons8_navigation_90___),
                flat = true,
                rotation = SphericalUtil.computeHeading(
                    polyline.first(),
                    polyline[polyline.indices.first+1]
                ).toFloat()-45
            )
        }
    }
    AnimatedVisibility(
        visible = deviceDetails.mapLoaded,
        modifier = modifier,
        exit = fadeOut(),
        enter = EnterTransition.None
    ) {
        Box(modifier = modifier) {
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
                            .align(Alignment.BottomCenter),
                        mainViewModel = mainViewModel,
                        sessionViewModel = sessionViewModel,
                        snackbarHostState = snackbarHostState,
                        session = session
                    )
                }
                else -> {
                    when (val s = tripViewModel.getCourierTripState) {
                        GetCourierTripState.Loading -> {
                            Loader(
                                modifier = Modifier
                                    .align(Alignment.Center)
                            )
                        }
                        is GetCourierTripState.Success -> {
                            if (s.trip != null) {
                                TripScreen(
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter),
                                    tripViewModel = tripViewModel,
                                    trip = s.trip
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
