package com.lomolo.uzicourier.compose.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import com.lomolo.uzicourier.DeviceDetailsUiState
import com.lomolo.uzicourier.MainViewModel
import com.lomolo.uzicourier.compose.loader.Loader
import com.lomolo.uzicourier.compose.navigation.Navigation
import com.lomolo.uzicourier.compose.signin.SessionViewModel
import com.lomolo.uzicourier.model.Session
import com.lomolo.uzicourier.model.Trip

object HomeScreenDestination: Navigation {
    override val route = "home"
    override val title = null
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
    tripViewModel: TripViewModel,
    sessionViewModel: SessionViewModel,
    onGetStartedClick: () -> Unit = {},
    onNavigateTo: (String) -> Unit = {},
    session: Session,
    assignment: Trip
) {
    val deviceDetails by mainViewModel.deviceDetailsUiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(session) {
        if (session.id.isNotBlank()) {
            tripViewModel.getTripAssignment(session.id)
        }
    }

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
                        session = session,
                        assignment = assignment,
                        tripViewModel = tripViewModel
                    )
                }
            }
        }
    }
}