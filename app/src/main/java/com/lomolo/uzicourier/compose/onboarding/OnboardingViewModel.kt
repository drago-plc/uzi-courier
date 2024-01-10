package com.lomolo.uzicourier.compose.onboarding

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.exception.ApolloException
import com.lomolo.uzicourier.GetCourierDocumentsQuery
import com.lomolo.uzicourier.network.UziGqlApiInterface
import com.lomolo.uzicourier.network.UziRestApiServiceInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.toImmutableMap
import java.io.IOException
import java.io.InputStream

class OnboardingViewModel(
    private val uziGqlApiRepository: UziGqlApiInterface,
    private val uziRestApiService: UziRestApiServiceInterface
): ViewModel() {
    private val _imageUploads = MutableStateFlow(ImageUploads())
    val imageUploadsUiState: StateFlow<ImageUploads> = _imageUploads.asStateFlow()

    var getCourierDocumentsUiState: GetCourierDocumentsState by mutableStateOf(GetCourierDocumentsState.Success(listOf()))
        private set

    suspend fun getCourierDocuments() {
        getCourierDocumentsUiState = GetCourierDocumentsState.Loading
        viewModelScope.launch {
            getCourierDocumentsUiState = try {
                val res = uziGqlApiRepository.getCourierDocuments().dataOrThrow()
                GetCourierDocumentsState.Success(res.getCourierDocuments)
            } catch (e: ApolloException) {
                GetCourierDocumentsState.Error(e.message)
            }
        }
    }

    fun createCourierUpload(key: String) {
    }

    fun uploadImage(key: String, stream: InputStream) {
        val request = stream.readBytes().toRequestBody()
        val filePart = MultipartBody.Part.createFormData(
            "file",
            "photo_${System.currentTimeMillis()}.jpg",
            request
        )
        _imageUploads.update {
            val uplds = it.uploads.toMutableMap()
            uplds.set(key, ImageState.Loading)
            it.copy(uploads = uplds.toImmutableMap())
        }
        viewModelScope.launch {
            try {
                val res = uziRestApiService.uploadImage(filePart)
                _imageUploads.update {
                    val uplds = it.uploads.toMutableMap()
                    uplds.set(key, ImageState.Success(res.imageUri))
                    it.copy(uploads = uplds.toImmutableMap())
                }
            } catch(e: IOException) {
                _imageUploads.update {
                    val uplds = it.uploads.toMutableMap()
                    uplds.set(key, ImageState.Error(e.message))
                    it.copy(uploads = uplds.toImmutableMap())
                }
            }
        }
    }
}

interface ImageState {
    data class Success(val uri: String? = null): ImageState
    data object Loading: ImageState
    data class Error(val message: String?): ImageState
}

interface GetCourierDocumentsState {
    data class Success(val data: List<GetCourierDocumentsQuery.GetCourierDocument>): GetCourierDocumentsState
    data object Loading: GetCourierDocumentsState
    data class Error(val message: String?): GetCourierDocumentsState
}

data class ImageUploads(
    val uploads: Map<String, ImageState> = mapOf()
)