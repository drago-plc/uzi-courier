package com.lomolo.uzicourier.compose.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lomolo.uzicourier.compose.navigation.Navigation

object PoliceClearanceDocumentDestination: Navigation {
    override val route = "police_clearance_doc"
    override val title = null
}

@Composable
fun PoliceClearanceDocument(
    modifier: Modifier = Modifier
) {
    UploadDocumentScreen(
        modifier = modifier
    )
}