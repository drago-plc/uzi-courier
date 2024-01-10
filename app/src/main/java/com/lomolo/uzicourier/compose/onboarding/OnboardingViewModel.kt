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
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.io.InputStream

class OnboardingViewModel(
    private val uziGqlApiRepository: UziGqlApiInterface,
    private val uziRestApiService: UziRestApiServiceInterface
): ViewModel() {
    var imageUiState: ImageState by mutableStateOf(ImageState.Success())
        private set
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

    fun createCourierUpload() {
        imageUiState = ImageState.Loading
        viewModelScope.launch {
            imageUiState = try {
                ImageState.Success()
            } catch(e: ApolloException) {
                e.printStackTrace()
                ImageState.Error(e.message)
            }
        }
    }

    fun uploadImage(stream: InputStream) {
        val request = stream.readBytes().toRequestBody()
        val filePart = MultipartBody.Part.createFormData(
            "file",
            "photo_${System.currentTimeMillis()}.jpg",
            request
        )
        imageUiState = ImageState.Loading
        viewModelScope.launch {
            imageUiState = try {
                val res = uziRestApiService.uploadImage(filePart)
                ImageState.Success(res.imageUri)
            } catch(e: IOException) {
                ImageState.Error(e.message)
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