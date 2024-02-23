package com.lomolo.uzicourier.compose.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.lomolo.uzicourier.DeviceDetails
import com.lomolo.uzicourier.MainViewModel
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
        mutableStateOf(MapUiSettings(zoomControlsEnabled = false))
    }
    val mapProperties by remember {
        mutableStateOf(MapProperties(mapType = MapType.TERRAIN))
    }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(deviceDetails.gps, 17f)
    }

    GoogleMap(
        modifier = modifier,
        properties = mapProperties,
        onMapLoaded = { mainViewModel.setMapLoaded(true) },
        uiSettings = uiSettings,
        cameraPositionState = cameraPositionState
    )
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
                    TripScreen(
                        modifier = Modifier
                            .align(Alignment.BottomCenter),
                        tripViewModel = tripViewModel,
                        cameraPositionState = cameraPositionState,
                        mapLoaded = deviceDetails.mapLoaded
                    )
                }
            }
        }
    }
}
