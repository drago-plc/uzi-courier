package com.lomolo.uzicourier

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.lomolo.uzicourier.compose.onboarding.OnboardingViewModel
import com.lomolo.uzicourier.compose.signin.SessionViewModel

/*
 * Uzi app view model factory provider
 */
object UziViewModelProvider {
    val Factory = viewModelFactory {
        lateinit var mainViewModel: MainViewModel

        initializer {
            mainViewModel = MainViewModel(uziApplication().container.uziRestApiService)
            mainViewModel
        }
        initializer {
            SessionViewModel(
                uziApplication().container.sessionRepository,
                mainViewModel
            )
        }
        initializer {
            OnboardingViewModel(
                uziApplication().container.uziGqlApiRepository
            )
        }
    }
}

/*
 * Grab instance of uzi application
 */
fun CreationExtras.uziApplication(): UziCourierApp = (this[AndroidViewModelFactory.APPLICATION_KEY] as UziCourierApp)