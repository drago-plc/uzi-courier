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
import com.lomolo.uzicourier.type.UploadFile
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

    private val _createDocument = MutableStateFlow(CreateDocumentsState())
    val createDocumentUiState: StateFlow<CreateDocumentsState> = _createDocument.asStateFlow()

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

    fun createCourierUpload(type: UploadFile, uri: String, cb: () -> Unit = {}) {
        _createDocument.update {
            val createDoc = it.state.toMutableMap()
            createDoc[type.toString()] = CreateCourierDocumentState.Loading
            it.copy(state = createDoc.toImmutableMap())
        }
        viewModelScope.launch {
            try {
                val res = uziGqlApiRepository.createCourierDocument(type = type, uri = uri).dataOrThrow()
                _createDocument.update {
                    val createDoc = it.state.toMutableMap()
                    createDoc[type.toString()] = CreateCourierDocumentState.Success(res.createCourierDocument)
                    it.copy(state = createDoc.toImmutableMap())
                }.also { cb() }
            } catch(e: ApolloException) {
                _createDocument.update {
                    val createDoc = it.state.toMutableMap()
                    createDoc[type.toString()] = CreateCourierDocumentState.Error(e.message)
                    it.copy(state = createDoc.toImmutableMap())
                }

            }
        }
    }

    fun uploadImage(key: UploadFile, stream: InputStream) {
        val request = stream.readBytes().toRequestBody()
        val filePart = MultipartBody.Part.createFormData(
            "file",
            "${key}_${System.currentTimeMillis()}.jpg",
            request
        )
        _imageUploads.update {
            val uplds = it.uploads.toMutableMap()
            uplds[key.toString()] = ImageState.Loading
            it.copy(uploads = uplds.toImmutableMap())
        }
        viewModelScope.launch {
            try {
                val res = uziRestApiService.uploadImage(filePart)
                _imageUploads.update {
                    val uplds = it.uploads.toMutableMap()
                    uplds[key.toString()] = ImageState.Success(res.imageUri)
                    it.copy(uploads = uplds.toImmutableMap())
                }
            } catch(e: IOException) {
                _imageUploads.update {
                    val uplds = it.uploads.toMutableMap()
                    uplds[key.toString()] = ImageState.Error(e.message)
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

interface CreateCourierDocumentState {
    data class Success(val success: Boolean): CreateCourierDocumentState
    data object Loading: CreateCourierDocumentState
    data class Error(val message: String?): CreateCourierDocumentState
}

data class CreateDocumentsState(
    val state: Map<String, CreateCourierDocumentState> = mapOf()
)