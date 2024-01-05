package com.lomolo.uzicourier.compose.onboarding

import androidx.lifecycle.ViewModel
import com.lomolo.uzicourier.network.UziGqlApiInterface

class OnboardingViewModel(
    private val uziGqlApiRepository: UziGqlApiInterface
): ViewModel() {
    suspend fun getCourierDocuments() = uziGqlApiRepository.getCourierDocuments().dataOrThrow()
}