package com.lomolo.uzicourier.compose.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.lomolo.uzicourier.compose.navigation.Navigation
import com.lomolo.uzicourier.compose.onboarding.OnboardingDestination
import com.lomolo.uzicourier.compose.onboarding.OnboardingScreen
import com.lomolo.uzicourier.compose.onboarding.OnboardingViewModel

object OnboardingGraph: Navigation {
    override val route = "graph/onboarding"
    override val title = null
}

fun NavGraphBuilder.onboarding(
    navController: NavHostController,
    onboardingViewModel: OnboardingViewModel
) {
    navigation(
        startDestination = OnboardingDestination.route,
        route = OnboardingGraph.route
    ) {
        composable(route = OnboardingDestination.route) {
            OnboardingScreen(
                onboardingViewModel = onboardingViewModel
            )
        }
    }
}