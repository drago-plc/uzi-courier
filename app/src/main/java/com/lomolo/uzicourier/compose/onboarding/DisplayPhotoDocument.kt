package com.lomolo.uzicourier.compose.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lomolo.uzicourier.R
import com.lomolo.uzicourier.compose.navigation.Navigation
import com.lomolo.uzicourier.type.UploadFile

object DisplayDocumentDestination: Navigation {
    override val route = "display_pic"
    override val title = R.string.profile_picture
}

@Composable
fun DisplayPhotoDocument(
    modifier: Modifier = Modifier,
    onboardingViewModel: OnboardingViewModel,
    type: UploadFile
) {
    UploadDocumentScreen(
        onboardingViewModel = onboardingViewModel,
        modifier = modifier,
        type = type
    )
}