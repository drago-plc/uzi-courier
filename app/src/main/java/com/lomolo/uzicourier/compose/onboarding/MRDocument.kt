package com.lomolo.uzicourier.compose.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lomolo.uzicourier.compose.navigation.Navigation

object MRDocumentDestination: Navigation {
    override val route = "motor_registration"
    override val title = null
}

@Composable
fun MRDocument(
    modifier: Modifier = Modifier,
    text: @Composable () -> Unit? = {}
) {
    UploadDocumentScreen(
        text = text
    )
}