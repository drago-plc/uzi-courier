package com.lomolo.uzicourier.compose.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lomolo.uzicourier.model.Trip
import com.lomolo.uzicourier.repository.TripInterface
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class TripViewModel(
    private val tripRepository: TripInterface
): ViewModel() {
    val tripUiState: StateFlow<Trip> = tripRepository
        .getTrip()
        .map {
            if (it.isEmpty()) {
                Trip()
            } else {
                Trip(
                    id = it[0].id,
                    status = it[0].status
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = Trip(),
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS)
        )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}