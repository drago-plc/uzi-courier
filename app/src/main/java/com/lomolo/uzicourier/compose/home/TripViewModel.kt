package com.lomolo.uzicourier.compose.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.exception.ApolloException
import com.lomolo.uzicourier.GetTripQuery
import com.lomolo.uzicourier.model.Trip
import com.lomolo.uzicourier.repository.TripInterface
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TripViewModel(
    private val tripRepository: TripInterface
): ViewModel() {
    val tripUiState: StateFlow<Trip> = tripRepository
        .getTrip()
        .filterNotNull()
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
    var getCourierTripState: GetCourierTripState by mutableStateOf(GetCourierTripState.Success(null))
        private set

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    fun getTripAssignment(userId: String) = tripRepository.getTripAssignment(userId)

    fun getCourierAssignedTrip() {
        getCourierTripState = GetCourierTripState.Loading
        viewModelScope.launch {
            getCourierTripState = try {
                val res = tripRepository.getCourierTrip(tripUiState.value.id).dataOrThrow()
                GetCourierTripState.Success(res.getTripDetails)
            } catch(e: ApolloException) {
                GetCourierTripState.Error(e.message)
            }
        }
    }

    fun clearTrips() = viewModelScope.launch { tripRepository.clearTrips() }
}

interface GetCourierTripState {
    data class Success(val trip: GetTripQuery.GetTripDetails?): GetCourierTripState
    data object Loading: GetCourierTripState
    data class Error(val message: String?): GetCourierTripState
}