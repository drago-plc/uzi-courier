package com.lomolo.uzicourier.compose.onboarding

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.exception.ApolloException
import com.lomolo.uzicourier.network.UziGqlApiInterface
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val uziGqlApiRepository: UziGqlApiInterface
): ViewModel() {
    var imageUiState: ImageState by mutableStateOf(ImageState.Success())
        private set

    suspend fun getCourierDocuments() = uziGqlApiRepository.getCourierDocuments().dataOrThrow()

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
}

interface ImageState {
    data class Success(val uri: String? = null): ImageState
    data object Loading: ImageState
    data class Error(val message: String?): ImageState
}