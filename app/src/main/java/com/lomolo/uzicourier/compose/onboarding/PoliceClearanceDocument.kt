package com.lomolo.uzicourier.compose.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lomolo.uzicourier.compose.navigation.Navigation
import com.lomolo.uzicourier.type.UploadFile

object PoliceClearanceDocumentDestination: Navigation {
    override val route = "police_clearance_doc"
    override val title = "Police clearance"
}

@Composable
fun PoliceClearanceDocument(
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