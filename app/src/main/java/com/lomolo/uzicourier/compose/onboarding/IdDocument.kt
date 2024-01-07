package com.lomolo.uzicourier.compose.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lomolo.uzicourier.compose.navigation.Navigation

object IdDocumentDestination: Navigation {
    override val route = "id_document"
    override val title = null
}

@Composable
fun IdDocument(
    modifier: Modifier = Modifier
) {
    UploadDocumentScreen(
        modifier = modifier
    )
}