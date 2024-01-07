package com.lomolo.uzicourier.compose.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.lomolo.uzicourier.R
import com.lomolo.uzicourier.ui.theme.UziCourierTheme

@Composable
fun UploadDocumentScreen(
    modifier: Modifier = Modifier,
    text: @Composable () -> Unit? = {},
    onProceedClick: () -> Unit = {}
) {
    val imageUri = "https://uzi-images.s3.eu-west-2.amazonaws.com/20220222_143429.jpg"

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        text()
        AsyncImage(
            model = ImageRequest
                .Builder(LocalContext.current)
                .data(imageUri)
                .crossfade(true)
                .build(),
            contentDescription = null,
            placeholder = painterResource(R.drawable.loading_img),
            error = painterResource(R.drawable.ic_broken_image),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Preview
@Composable
fun UploadDocumentPreview() {
    UziCourierTheme {
        UploadDocumentScreen()
    }
}