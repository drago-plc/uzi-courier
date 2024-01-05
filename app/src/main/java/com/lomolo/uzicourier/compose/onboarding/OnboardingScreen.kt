package com.lomolo.uzicourier.compose.onboarding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lomolo.uzicourier.ui.theme.UziCourierTheme

private val docs = listOf(
    "Motorcycle Registration Certificate",
    "Profile Photo",
    "Police Clearance",
    "Identification Document"
)
@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Heading(
            modifier = Modifier
                .fillMaxWidth(),
            text = "Let's get you setup"
        )
    }
}

@Composable
private fun Heading(
    modifier: Modifier = Modifier,
    text: String
) {
    Row(
        modifier = modifier,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Preview
@Composable
fun OnboardingPreview() {
    UziCourierTheme {
        OnboardingScreen()
    }
}