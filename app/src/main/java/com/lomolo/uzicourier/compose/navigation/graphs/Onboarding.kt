package com.lomolo.uzicourier.compose.navigation.graphs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.lomolo.uzicourier.R
import com.lomolo.uzicourier.compose.BottomBar
import com.lomolo.uzicourier.compose.TopBar
import com.lomolo.uzicourier.compose.navigation.Navigation
import com.lomolo.uzicourier.compose.onboarding.DisplayDocumentDestination
import com.lomolo.uzicourier.compose.onboarding.DisplayPhotoDocument
import com.lomolo.uzicourier.compose.onboarding.IdDocument
import com.lomolo.uzicourier.compose.onboarding.IdDocumentDestination
import com.lomolo.uzicourier.compose.onboarding.MRDocument
import com.lomolo.uzicourier.compose.onboarding.MRDocumentDestination
import com.lomolo.uzicourier.compose.onboarding.OnboardingDestination
import com.lomolo.uzicourier.compose.onboarding.OnboardingScreen
import com.lomolo.uzicourier.compose.onboarding.OnboardingViewModel
import com.lomolo.uzicourier.compose.onboarding.PoliceClearanceDocument
import com.lomolo.uzicourier.compose.onboarding.PoliceClearanceDocumentDestination

object OnboardingGraph: Navigation {
    override val route = "graph/onboarding"
    override val title = null
}

@OptIn(ExperimentalMaterial3Api::class)
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
                onboardingViewModel = onboardingViewModel,
                onNavigateTo = {
                    navController.navigate(it)
                }
            )
        }
        composable(route = DisplayDocumentDestination.route) {
            Scaffold(
                topBar = {
                    TopBar(
                        title = DisplayDocumentDestination.title,
                        canNavigateBack = true,
                        navigateBack = {
                            navController.popBackStack()
                        }
                    )
                },
                bottomBar = {
                    BottomBar {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .padding(start = 8.dp, end = 8.dp),
                            onClick = { /*TODO*/ },
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                text = stringResource(R.string.submit)
                            )
                        }
                    }
                }
            ) {innerPadding ->
                Surface(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    DisplayPhotoDocument(
                        onboardingViewModel = onboardingViewModel
                    )
                }
            }
        }
        composable(route = PoliceClearanceDocumentDestination.route) {
            Scaffold(
                topBar = {
                    TopBar(
                        title = PoliceClearanceDocumentDestination.title,
                        canNavigateBack = true,
                        navigateBack = {
                            navController.popBackStack()
                        }
                    )
                },
                bottomBar = {
                    BottomBar {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .padding(start = 8.dp, end = 8.dp),
                            onClick = { /*TODO*/ },
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                text = stringResource(R.string.submit)
                            )
                        }
                    }
                }
            ) {innerPadding ->
                Surface(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    PoliceClearanceDocument(
                        onboardingViewModel = onboardingViewModel
                    )
                }
            }
        }
        composable(route = IdDocumentDestination.route) {
            Scaffold(
                topBar = {
                    TopBar(
                        title = IdDocumentDestination.title,
                        canNavigateBack = true,
                        navigateBack = {
                            navController.popBackStack()
                        }
                    )
                },
                bottomBar = {
                    BottomBar {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .padding(start = 8.dp, end = 8.dp),
                            onClick = { /*TODO*/ },
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                text = stringResource(R.string.submit)
                            )
                        }
                    }
                }
            ) {innerPadding ->
                Surface(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    IdDocument(
                        onboardingViewModel = onboardingViewModel
                    )
                }
            }
        }
        composable(route = MRDocumentDestination.route) {
            Scaffold(
                topBar = {
                    TopBar(
                        title = MRDocumentDestination.title,
                        canNavigateBack = true,
                        navigateBack = {
                            navController.popBackStack()
                        }
                    )
                },
                bottomBar = {
                    BottomBar {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .padding(start = 8.dp, end = 8.dp),
                            onClick = { /*TODO*/ },
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                text = stringResource(R.string.submit)
                            )
                        }
                    }
                }
            ) {innerPadding ->
                Surface(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    MRDocument(
                        onboardingViewModel = onboardingViewModel
                    )
                }
            }
        }
    }
}