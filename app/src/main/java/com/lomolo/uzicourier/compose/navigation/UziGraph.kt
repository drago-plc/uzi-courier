package com.lomolo.uzicourier.compose.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lomolo.uzicourier.MainViewModel
import com.lomolo.uzicourier.UziViewModelProvider
import com.lomolo.uzicourier.compose.home.HomeScreen
import com.lomolo.uzicourier.compose.home.HomeScreenDestination
import com.lomolo.uzicourier.compose.navigation.graphs.UserGraphDestination
import com.lomolo.uzicourier.compose.navigation.graphs.onboarding
import com.lomolo.uzicourier.compose.navigation.graphs.user
import com.lomolo.uzicourier.compose.onboarding.OnboardingViewModel
import com.lomolo.uzicourier.compose.signin.SessionViewModel

@Composable
fun UziCourierNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    mainViewModel: MainViewModel = viewModel(factory = UziViewModelProvider.Factory),
    sessionViewModel: SessionViewModel = viewModel(factory = UziViewModelProvider.Factory),
    onboardingViewModel: OnboardingViewModel = viewModel(factory = UziViewModelProvider.Factory)
) {
    val session by sessionViewModel.sessionUiState.collectAsState()
    val createDocumentUiState by onboardingViewModel.createDocumentUiState.collectAsState()

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
                        session = session,
                        onGetStartedClick = { navController.navigate(UserGraphDestination.route) },
                        onNavigateTo = {
                            navController.navigate(it) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // re-selecting the same item
                                launchSingleTop = true
                                // Restore state when re-selecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
        user(
            navController = navController,
            sessionViewModel = sessionViewModel,
            mainViewModel = mainViewModel
        )
        onboarding(
            navController = navController,
            onboardingViewModel = onboardingViewModel,
            createDocumentUiState = createDocumentUiState
        )
    }
}