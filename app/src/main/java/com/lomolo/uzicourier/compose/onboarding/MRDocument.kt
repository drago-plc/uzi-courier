package com.lomolo.uzicourier.compose.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lomolo.uzicourier.R
import com.lomolo.uzicourier.compose.navigation.Navigation
import com.lomolo.uzicourier.type.UploadFile

object MRDocumentDestination: Navigation {
    override val route = "motor_registration"
    override val title = R.string.mcr_reg_cert
}

@Composable
fun MRDocument(
    modifier: Modifier = Modifier,
    text: @Composable () -> Unit? = {},
    onboardingViewModel: OnboardingViewModel,
    type: UploadFile
) {
    UploadDocumentScreen(
        modifier = modifier,
        text = text,
        onboardingViewModel = onboardingViewModel,
        type = type
    )
}