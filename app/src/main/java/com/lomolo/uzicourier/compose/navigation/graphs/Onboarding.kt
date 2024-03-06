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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.lomolo.uzicourier.R
import com.lomolo.uzicourier.compose.BottomBar
import com.lomolo.uzicourier.compose.TopBar
import com.lomolo.uzicourier.compose.loader.Loader
import com.lomolo.uzicourier.compose.navigation.Navigation
import com.lomolo.uzicourier.compose.onboarding.CreateCourierDocumentState
import com.lomolo.uzicourier.compose.onboarding.DisplayDocumentDestination
import com.lomolo.uzicourier.compose.onboarding.DisplayPhotoDocument
import com.lomolo.uzicourier.compose.onboarding.IdDocument
import com.lomolo.uzicourier.compose.onboarding.IdDocumentDestination
import com.lomolo.uzicourier.compose.onboarding.ImageState
import com.lomolo.uzicourier.compose.onboarding.MRDocument
import com.lomolo.uzicourier.compose.onboarding.MRDocumentDestination
import com.lomolo.uzicourier.compose.onboarding.OnboardingDestination
import com.lomolo.uzicourier.compose.onboarding.OnboardingScreen
import com.lomolo.uzicourier.compose.onboarding.OnboardingViewModel
import com.lomolo.uzicourier.compose.onboarding.PoliceClearanceDocument
import com.lomolo.uzicourier.compose.onboarding.PoliceClearanceDocumentDestination
import com.lomolo.uzicourier.compose.signin.SessionViewModel
import com.lomolo.uzicourier.type.UploadFile

object OnboardingGraph: Navigation {
    override val route = "graph/onboarding"
    override val title = null
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.onboarding(
    navController: NavHostController,
    sessionViewModel: SessionViewModel,
    onboardingViewModel: OnboardingViewModel,
) {
    navigation(
        startDestination = OnboardingDestination.route,
        route = OnboardingGraph.route
    ) {
        composable(route = OnboardingDestination.route) {
            OnboardingScreen(
                onboardingViewModel = onboardingViewModel,
                sessionViewModel = sessionViewModel,
                onNavigateTo = {
                    navController.navigate(it)
                }
            )
        }
        composable(route = DisplayDocumentDestination.route) {
            Scaffold(
                topBar = {
                    TopBar(
                        title = stringResource(DisplayDocumentDestination.title),
                        canNavigateBack = true,
                        navigateBack = {
                            navController.popBackStack()
                        }
                    )
                },
                bottomBar = {
                    BottomBar(
                        type = UploadFile.DP,
                        onboardingViewModel = onboardingViewModel,
                        onNavigateUp = {
                            navController.popBackStack()
                        }
                    )
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
                        title = stringResource(PoliceClearanceDocumentDestination.title),
                        canNavigateBack = true,
                        navigateBack = {
                            navController.popBackStack()
                        }
                    )
                },
                bottomBar = {
                    BottomBar(
                        type = UploadFile.PC,
                        onboardingViewModel = onboardingViewModel,
                        onNavigateUp = {
                            navController.popBackStack()
                        }
                    )
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
                        title = stringResource(IdDocumentDestination.title),
                        canNavigateBack = true,
                        navigateBack = {
                            navController.popBackStack()
                        }
                    )
                },
                bottomBar = {
                    BottomBar(
                        type = UploadFile.ID,
                        onboardingViewModel = onboardingViewModel,
                        onNavigateUp = {
                            navController.popBackStack()
                        }
                    )
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
                        title = stringResource(MRDocumentDestination.title),
                        canNavigateBack = true,
                        navigateBack = {
                            navController.popBackStack()
                        }
                    )
                },
                bottomBar = {
                    BottomBar(
                        type = UploadFile.MCR,
                        onboardingViewModel = onboardingViewModel,
                        onNavigateUp = {
                            navController.popBackStack()
                        }
                    )
                }
            ) {innerPadding ->
                Surface(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    MRDocument(
                        onboardingViewModel = onboardingViewModel,
                        type = UploadFile.MCR,
                        text = {
                            Text(
                                text = stringResource(R.string.mcr_doc_text),
                                modifier = Modifier
                                    .padding(20.dp)
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomBar(
    modifier: Modifier = Modifier,
    onboardingViewModel: OnboardingViewModel,
    type: UploadFile,
    onNavigateUp: () -> Unit = {}
) {
    val imageUploads by onboardingViewModel.imageUploadsUiState.collectAsState()
    val createDocumentUiState by onboardingViewModel.createDocumentUiState.collectAsState()
    val uri = when(val s = imageUploads.uploads[type.toString()]) {
        is ImageState.Success -> {
            s.uri
        }
        else -> {null}
    }
    val documentState = createDocumentUiState.state[type.toString()]

    BottomBar {
        Row(
            modifier = modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            when(documentState) {
                is CreateCourierDocumentState.Loading -> {
                    Loader()
                }
                is CreateCourierDocumentState.Error -> {
                    Text(
                        text = stringResource(id = R.string.not_your_fault_err),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall
                    )
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .padding(start = 8.dp, end = 8.dp),
                        onClick = {
                            onboardingViewModel.createCourierUpload(type, uri!!) {
                                onNavigateUp()
                            }
                        },
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = stringResource(R.string.retry),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
                else -> {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .padding(start = 8.dp, end = 8.dp),
                        onClick = {
                            if (uri != null && documentState !is CreateCourierDocumentState.Loading) {
                                onboardingViewModel.createCourierUpload(type, uri) {
                                    onNavigateUp()
                                }
                            }
                        },
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = stringResource(R.string.submit),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }
    }
}