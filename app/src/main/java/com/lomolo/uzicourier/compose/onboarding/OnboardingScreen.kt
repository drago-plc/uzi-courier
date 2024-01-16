package com.lomolo.uzicourier.compose.onboarding

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.twotone.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lomolo.uzicourier.GetCourierDocumentsQuery
import com.lomolo.uzicourier.R
import com.lomolo.uzicourier.compose.RetryErrorScreen
import com.lomolo.uzicourier.compose.loader.Loader
import com.lomolo.uzicourier.compose.navigation.Navigation
import com.lomolo.uzicourier.type.UploadVerificationStatus
import kotlinx.coroutines.launch

object OnboardingDestination: Navigation {
    override val route = "onboarding"
    override val title = null
}

private data class Doc(
    val name: String,
    val required: Boolean = false,
    val type: String,
    val route: String
)

private val docs = listOf(
    Doc("Motorcycle Registration Certificate", type = "MCR", route = MRDocumentDestination.route),
    Doc("Profile Photo", true, type = "DP", route = DisplayDocumentDestination.route),
    Doc("Police Clearance", true, type = "PC", route = PoliceClearanceDocumentDestination.route),
    Doc("Identification Document", true, type = "ID", route = IdDocumentDestination.route)
)

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    onboardingViewModel: OnboardingViewModel,
    onNavigateTo: (String) -> Unit = {}
) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        onboardingViewModel.getCourierDocuments()
    }

    when(val s = onboardingViewModel.getCourierDocumentsUiState) {
        is GetCourierDocumentsState.Success -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Heading(
                    modifier = Modifier
                        .fillMaxWidth(),
                    heading = stringResource(R.string.let_s_get_you_setup),
                    subHeading = stringResource(R.string.lets_get_you_setup_subheading)
                )
                RequiredDocuments(
                    modifier = Modifier
                        .padding(top = 16.dp),
                    docs = docs,
                    courierDocs = s.data,
                    onNavigateTo = onNavigateTo
                )
            }
        }
        is GetCourierDocumentsState.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Loader()
            }
        }
        is GetCourierDocumentsState.Error -> {
           RetryErrorScreen(
               onRetry = { scope.launch { onboardingViewModel.getCourierDocuments() } }
           )
        }
    }
}

@Composable
private fun Heading(
    modifier: Modifier = Modifier,
    heading: String,
    subHeading: String
) {
    Column(
        modifier = modifier
    ){
        Text(
            text = heading,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = subHeading,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(top = 16.dp)
        )
    }
}

@Composable
private fun RequiredDocuments(
    modifier: Modifier = Modifier,
    docs: List<Doc>,
    courierDocs: List<GetCourierDocumentsQuery.GetCourierDocument>,
    onNavigateTo: (String) -> Unit = {}
) {
    Column(
        modifier = modifier
    ) {
        docs.forEach { doc ->
            val document = GetDocumentData(courierDocs, doc.type)

            ListItem(
                headlineContent = {
                    Text(
                        text = doc.name
                    )
                },
                supportingContent = {
                    if (document?.verification == UploadVerificationStatus.REJECTED) {
                        Text(
                            text = "Previous submission rejected. Resubmit document for verification.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                trailingContent = {
                    if (courierDocs.isNotEmpty()) {
                        when(document?.verification) {
                            UploadVerificationStatus.ONBOARDING -> {
                                Icon(
                                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null
                                )
                            }
                            UploadVerificationStatus.VERIFYING -> {
                                Text(
                                    text = stringResource(R.string.verifying),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            UploadVerificationStatus.VERIFIED -> {
                                Icon(
                                    Icons.TwoTone.CheckCircle,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            UploadVerificationStatus.REJECTED -> {
                                Icon(
                                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null
                                )
                            }
                            else -> {
                                Icon(
                                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null
                                )
                            }
                        }
                    } else {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        println(document)
                        val allowClick = (document == null) || (courierDocs.isEmpty()) || (document.verification.toString() == UploadVerificationStatus.REJECTED.toString() || document.verification == UploadVerificationStatus.ONBOARDING)
                        if (allowClick) {
                            onNavigateTo(doc.route)
                        }
                    }
            )
        }
    }
}

private fun GetDocumentData(docs: List<GetCourierDocumentsQuery.GetCourierDocument>, type: String): GetCourierDocumentsQuery.GetCourierDocument? {
    return docs.find {
        it.type == type
    }
}