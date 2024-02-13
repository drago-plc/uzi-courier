package com.lomolo.uzicourier.compose.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.lomolo.uzicourier.DeviceDetails
import com.lomolo.uzicourier.DeviceDetailsUiState
import com.lomolo.uzicourier.MainViewModel
import com.lomolo.uzicourier.R
import com.lomolo.uzicourier.compose.loader.Loader
import com.lomolo.uzicourier.compose.navigation.Navigation
import com.lomolo.uzicourier.compose.onboarding.OnboardingDestination
import com.lomolo.uzicourier.compose.signin.GetStarted
import com.lomolo.uzicourier.compose.signin.SessionViewModel
import com.lomolo.uzicourier.compose.signin.UserNameDestination
import com.lomolo.uzicourier.model.CourierStatus
import com.lomolo.uzicourier.model.Session
import kotlinx.coroutines.launch

object HomeScreenDestination: Navigation {
    override val route = "home"
    override val title = null
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
    sessionViewModel: SessionViewModel,
    onGetStartedClick: () -> Unit = {},
    onNavigateTo: (String) -> Unit = {},
    session: Session
) {
    val deviceDetails by mainViewModel.deviceDetailsUiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        }
    ) { innerPadding ->
        Box(
            modifier
                .fillMaxSize()
                .padding(innerPadding)) {
            when(mainViewModel.deviceDetailsState) {
                DeviceDetailsUiState.Loading -> Loader(
                    modifier = Modifier.matchParentSize()
                )
                is DeviceDetailsUiState.Error -> {
                    HomeErrorScreen(
                        mainViewModel = mainViewModel,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                DeviceDetailsUiState.Success -> {
                    HomeSuccessScreen(
                        modifier = Modifier.matchParentSize(),
                        snackbarHostState = snackbarHostState,
                        mainViewModel = mainViewModel,
                        sessionViewModel = sessionViewModel,
                        deviceDetails = deviceDetails,
                        onGetStartedClick = onGetStartedClick,
                        onNavigateTo = onNavigateTo,
                        session = session
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeSuccessScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
    sessionViewModel: SessionViewModel,
    snackbarHostState: SnackbarHostState,
    deviceDetails: DeviceDetails,
    onGetStartedClick: () -> Unit,
    session: Session,
    onNavigateTo: (String) -> Unit = {},
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
                session = session
           )
        }
    }
}

@Composable
private fun DefaultHomeScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
    snackbarHostState: SnackbarHostState,
    sessionViewModel: SessionViewModel,
    deviceDetails: DeviceDetails,
    onGetStartedClick: () -> Unit,
    session: Session
) {
    val scope = rememberCoroutineScope()
    val isAuthed = session.token.isNotBlank()
    val uiSettings by remember {
        mutableStateOf(MapUiSettings(zoomControlsEnabled = false))
    }
    val status = when(session.courierStatus) {
        CourierStatus.OFFLINE -> {CourierStatus.OFFLINE.toString()}
        CourierStatus.ONLINE -> {CourierStatus.OFFLINE.toString()}
        else -> {CourierStatus.OFFLINE.toString()}
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
            if (!isAuthed) {
                Box(
                    modifier = Modifier
                        .padding(bottom = 28.dp, start = 8.dp, end = 8.dp)
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .wrapContentHeight()
                ) {
                    GetStarted(
                        onGetStartedClick = onGetStartedClick
                    )
                }
            }
            // TODO only show if courier is not on a trip
            if (isAuthed) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (session.courierStatus == CourierStatus.OFFLINE) {
                            Text(
                                "Ready to work?",
                                style = MaterialTheme.typography.titleMedium
                            )
                        } else {
                            Text(
                                "Go offline?",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            onClick = {
                                val s = if (session.courierStatus == CourierStatus.OFFLINE)
                                    CourierStatus.ONLINE.toString()
                                else
                                    CourierStatus.OFFLINE.toString()

                                mainViewModel.setCourierStatus(s) {
                                    sessionViewModel.refreshSession()
                                    scope.launch { snackbarHostState.showSnackbar("You are now ${s.lowercase()}!") }
                                }
                            },
                            contentPadding = PaddingValues(16.dp),
                            colors = ButtonColors(
                                containerColor = if (session.courierStatus == CourierStatus.ONLINE) Color(0xff1b7f37) else ButtonDefaults.buttonColors().containerColor,
                                contentColor = ButtonDefaults.buttonColors().contentColor,
                                disabledContainerColor = ButtonDefaults.buttonColors().disabledContainerColor,
                                disabledContentColor = ButtonDefaults.buttonColors().disabledContentColor
                            ),
                            modifier = Modifier
                                .padding(16.dp),
                            shape = MaterialTheme.shapes.extraSmall
                        ) {
                            Text(
                                text = "Go",
                                style = MaterialTheme.typography.titleLarge,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeErrorScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.not_your_fault_err),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.error,
        )
        Button(
            onClick = { mainViewModel.getIpinfo() },
        ) {
            Text(
                text = stringResource(R.string.retry),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}