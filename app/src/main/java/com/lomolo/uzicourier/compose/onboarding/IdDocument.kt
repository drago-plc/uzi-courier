package com.lomolo.uzicourier.compose.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lomolo.uzicourier.compose.navigation.Navigation

object IdDocumentDestination: Navigation {
    override val route = "id_document"
    override val title = "Government issued ID"
}

@Composable
fun IdDocument(
    modifier: Modifier = Modifier,
    onboardingViewModel: OnboardingViewModel
) {
    UploadDocumentScreen(
        modifier = modifier,
        onboardingViewModel = onboardingViewModel,
        key = "ID"
    )
}