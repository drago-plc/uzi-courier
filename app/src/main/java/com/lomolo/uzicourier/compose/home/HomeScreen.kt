package com.lomolo.uzicourier.compose.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.lomolo.uzicourier.compose.signin.GetStarted

object HomeScreenDestination: Navigation {
    override val route = "home"
    override val title = null
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = viewModel(),
    isAuthed: Boolean,
    onGetStartedClick: () -> Unit = {}
) {
    val deviceDetails by mainViewModel.deviceDetailsUiState.collectAsState()

    Box(modifier.fillMaxSize()) {
        when(mainViewModel.deviceDetailsState) {
            is DeviceDetailsUiState.Loading -> Loader(
                modifier = Modifier.matchParentSize()
            )
            is DeviceDetailsUiState.Error -> {
                HomeErrorScreen(
                    mainViewModel = mainViewModel,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            is DeviceDetailsUiState.Success -> {
                HomeSuccessScreen(
                    modifier = Modifier.matchParentSize(),
                    mainViewModel = mainViewModel,
                    deviceDetails = deviceDetails,
                    isAuthed = isAuthed,
                    onGetStartedClick = onGetStartedClick
                )
            }
        }
    }
}

@Composable
fun HomeSuccessScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
    deviceDetails: DeviceDetails,
    isAuthed: Boolean,
    onGetStartedClick: () -> Unit
) {
    when {
        isAuthed -> {
           AuthedHomeScreen(
               modifier = modifier,
               mainViewModel = mainViewModel,
               deviceDetails = deviceDetails
           )
        }
        else -> {
            Box {
                Box(
                    modifier = modifier
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
        }
    }
}

@Composable
fun AuthedHomeScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
    deviceDetails: DeviceDetails
) {
    val uiSettings by remember {
        mutableStateOf(MapUiSettings(zoomControlsEnabled = false))
    }

    val mapProperties by remember {
        mutableStateOf(MapProperties(mapType = MapType.TERRAIN))
    }

    val cP = CameraPosition(deviceDetails.gps, 17f, 45f, 0f)
    val cameraPositionState = rememberCameraPositionState {
        position = cP
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
            Box(
                modifier = Modifier
                    .wrapContentHeight()
                    .align(Alignment.TopCenter)
                    .background(
                        MaterialTheme.colorScheme.background
                    )
            ) {
            }
        }
    }
}

@Composable
fun HomeErrorScreen(
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
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}