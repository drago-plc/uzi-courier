package com.lomolo.uzicourier.compose.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lomolo.uzicourier.compose.navigation.Navigation

object DisplayDocumentDestination: Navigation {
    override val route = "display_pic"
    override val title = "Profile picture"
}

@Composable
fun DisplayPhotoDocument(
    modifier: Modifier = Modifier,
    onboardingViewModel: OnboardingViewModel
) {
    UploadDocumentScreen(
        onboardingViewModel = onboardingViewModel,
        modifier = modifier,
        key = "DP"
    )
}