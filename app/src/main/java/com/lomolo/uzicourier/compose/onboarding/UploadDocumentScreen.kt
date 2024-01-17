package com.lomolo.uzicourier.compose.onboarding

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.lomolo.uzicourier.R
import com.lomolo.uzicourier.type.UploadFile
import kotlinx.coroutines.launch

@Composable
fun UploadDocumentScreen(
    modifier: Modifier = Modifier,
    text: @Composable () -> Unit? = {},
    onboardingViewModel: OnboardingViewModel,
    type: UploadFile
) {
    val uploads by onboardingViewModel.imageUploadsUiState.collectAsState()
    var uploadError: String? = null
    var imageUri: Any = R.drawable.image_gallery
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) {
        if (it != null) {
            val stream = context.contentResolver.openInputStream(it)
            if (stream != null) {
                onboardingViewModel.uploadImage(type, stream)
            }
        }
    }
    when(val s = uploads.uploads.get(type.toString())) {
        is ImageState.Loading -> {
            imageUri = R.drawable.loading_img
        }
        is ImageState.Error -> {
            uploadError = s.message
            imageUri = R.drawable.ic_broken_image
        }
        is ImageState.Success -> {
            if (s.uri != null) {
                imageUri = s.uri
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize(),
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
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clip(MaterialTheme.shapes.small)
                .size(360.dp)
                .clickable {
                    scope.launch {
                        pickMedia.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    }
                }
        )
        if (uploadError != null) {
            Text(
                text = uploadError,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}