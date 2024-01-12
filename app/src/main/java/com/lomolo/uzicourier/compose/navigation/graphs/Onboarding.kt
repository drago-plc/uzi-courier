package com.lomolo.uzicourier.compose.navigation.graphs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
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
import com.lomolo.uzicourier.compose.RetryErrorScreen
import com.lomolo.uzicourier.compose.TopBar
import com.lomolo.uzicourier.compose.loader.Loader
import com.lomolo.uzicourier.compose.navigation.Navigation
import com.lomolo.uzicourier.compose.onboarding.CreateCourierDocumentState
import com.lomolo.uzicourier.compose.onboarding.CreateDocumentsState
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
import com.lomolo.uzicourier.type.UploadFile

object OnboardingGraph: Navigation {
    override val route = "graph/onboarding"
    override val title = null
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.onboarding(
    navController: NavHostController,
    onboardingViewModel: OnboardingViewModel,
    createDocumentUiState: CreateDocumentsState
) {
    val documentCreateState = createDocumentUiState.state

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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        BottomBar {
                            when(val s = documentCreateState[UploadFile.DP.toString()]) {
                                CreateCourierDocumentState.Loading -> {
                                    Loader()
                                }
                                is CreateCourierDocumentState.Error -> {
                                    Text(
                                        text = stringResource(id = R.string.not_your_fault_err),
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                                else -> {
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
                        }
                    }
                }
            ) {innerPadding ->
                Surface(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    DisplayPhotoDocument(
                        onboardingViewModel = onboardingViewModel,
                        type = UploadFile.DP
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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        BottomBar {
                            when(val s = documentCreateState[UploadFile.PC.toString()]) {
                                CreateCourierDocumentState.Loading -> {
                                    Loader()
                                }
                                is CreateCourierDocumentState.Error -> {
                                    Text(
                                        text = stringResource(id = R.string.not_your_fault_err),
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                                else -> {
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
                        }
                    }
                }
            ) {innerPadding ->
                Surface(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    PoliceClearanceDocument(
                        onboardingViewModel = onboardingViewModel,
                        type = UploadFile.PC
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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        BottomBar {
                            when(val s = documentCreateState[UploadFile.ID.toString()]) {
                                CreateCourierDocumentState.Loading -> {
                                    Loader()
                                }
                                is CreateCourierDocumentState.Error -> {
                                    Text(
                                        text = stringResource(id = R.string.not_your_fault_err),
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                                else -> {
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
                        }
                    }
                }
            ) {innerPadding ->
                Surface(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    IdDocument(
                        onboardingViewModel = onboardingViewModel,
                        type = UploadFile.ID
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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        BottomBar {
                            when(val s = documentCreateState[UploadFile.MCR.toString()]) {
                                CreateCourierDocumentState.Loading -> {
                                    Loader()
                                }
                                is CreateCourierDocumentState.Error -> {
                                    Text(
                                        text = stringResource(id = R.string.not_your_fault_err),
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                                else -> {
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
                        }
                    }
                }
            ) {innerPadding ->
                Surface(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    MRDocument(
                        onboardingViewModel = onboardingViewModel,
                        type = UploadFile.MCR
                    )
                }
            }
        }
    }
}