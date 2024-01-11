package com.lomolo.uzicourier.compose.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lomolo.uzicourier.compose.navigation.Navigation
import com.lomolo.uzicourier.type.UploadFile

object IdDocumentDestination: Navigation {
    override val route = "id_document"
    override val title = "Government issued ID"
}

@Composable
fun IdDocument(
    modifier: Modifier = Modifier,
    onboardingViewModel: OnboardingViewModel,
    type: UploadFile
) {
    UploadDocumentScreen(
        modifier = modifier,
        onboardingViewModel = onboardingViewModel,
        type = type
    )
}