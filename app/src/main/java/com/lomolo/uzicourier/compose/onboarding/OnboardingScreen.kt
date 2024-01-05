package com.lomolo.uzicourier.compose.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lomolo.uzicourier.R
import com.lomolo.uzicourier.compose.navigation.Navigation
import com.lomolo.uzicourier.ui.theme.UziCourierTheme

object OnboardingDestination: Navigation {
    override val route = "onboarding"
    override val title = null
}

private data class Doc(
    val name: String,
    val required: Boolean = false,
    val type: String
)

private val docs = listOf(
    Doc("Motorcycle Registration Certificate", type = "MCR"),
    Doc("Profile Photo", true, type = "DP"),
    Doc("Police Clearance", true, type = "PC"),
    Doc("Identification Document", true, type = "ID")
)

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    onboardingViewModel: OnboardingViewModel
) {
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
            docs = docs
        )
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
    docs: List<Doc>
) {
    Column(
        modifier = modifier
    ) {
        docs.forEach { doc ->
            ListItem(
                headlineContent = {
                    Text(
                        text = doc.name
                    )
                },
                trailingContent = {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .padding(8.dp)
            )
        }
    }
}

@Preview
@Composable
fun OnboardingPreview() {
    UziCourierTheme {
        OnboardingScreen(onboardingViewModel = viewModel())
    }
}