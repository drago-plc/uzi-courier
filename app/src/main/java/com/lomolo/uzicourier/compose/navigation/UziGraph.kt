package com.lomolo.uzicourier.compose.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lomolo.uzicourier.MainViewModel
import com.lomolo.uzicourier.UziViewModelProvider
import com.lomolo.uzicourier.compose.home.HomeScreen
import com.lomolo.uzicourier.compose.home.HomeScreenDestination
import com.lomolo.uzicourier.compose.navigation.graphs.UserGraphDestination
import com.lomolo.uzicourier.compose.navigation.graphs.user
import com.lomolo.uzicourier.compose.signin.SessionViewModel

@Composable
fun UziCourierNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    mainViewModel: MainViewModel = viewModel(factory = UziViewModelProvider.Factory),
    sessionViewModel: SessionViewModel = viewModel(factory = UziViewModelProvider.Factory),
) {
    val sessionUiState by sessionViewModel.sessionUiState.collectAsState()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = HomeScreenDestination.route
    ) {
        composable(route = HomeScreenDestination.route) {
            Scaffold { innerPadding ->
                Surface(
                    modifier = modifier
                        .padding(innerPadding)
                ) {
                    HomeScreen(
                        mainViewModel = mainViewModel,
                        isAuthed = sessionUiState.token.isNotBlank(),
                        onGetStartedClick = { navController.navigate(UserGraphDestination.route) }
                    )
                }
            }
        }
        user(
            navController = navController,
            sessionViewModel = sessionViewModel,
            mainViewModel = mainViewModel
        )
    }
}