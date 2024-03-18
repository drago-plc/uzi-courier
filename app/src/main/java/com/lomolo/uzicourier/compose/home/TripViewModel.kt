package com.lomolo.uzicourier.compose.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.exception.ApolloException
import com.google.android.gms.maps.model.LatLng
import com.lomolo.uzicourier.GetTripQuery
import com.lomolo.uzicourier.ReverseGeocodeQuery
import com.lomolo.uzicourier.model.Trip
import com.lomolo.uzicourier.network.UziGqlApiInterface
import com.lomolo.uzicourier.repository.TripInterface
import com.lomolo.uzicourier.type.TripStatus
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TripViewModel(
    private val uziGqlApiService: UziGqlApiInterface,
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
    var reverseGeocodeState: ReverseGeocodeState by mutableStateOf(ReverseGeocodeState.Success(null))
        private set
    var reportTripStatusState: ReportTripStatusState by mutableStateOf(ReportTripStatusState.Success(false))

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    fun getTripAssignment(userId: String) = tripRepository.getTripAssignment(userId)

    fun getCourierAssignedTrip(tripId: String) {
        if (getCourierTripState !is GetCourierTripState.Loading) {
            getCourierTripState = GetCourierTripState.Loading
            viewModelScope.launch {
                getCourierTripState = try {
                    val res = tripRepository.getCourierTrip(tripId).dataOrThrow()
                    GetCourierTripState.Success(res.getTripDetails)
                } catch (e: ApolloException) {
                    e.printStackTrace()
                    GetCourierTripState.Error(e.message)
                }
            }
        }
    }

    fun clearTrips() = viewModelScope.launch { tripRepository.clearTrips() }

    fun reverseGeocode(gps: LatLng) {
        reverseGeocodeState = ReverseGeocodeState.Loading
        viewModelScope.launch {
            reverseGeocodeState = try {
                val res = uziGqlApiService.reverseGeocode(gps).dataOrThrow()
                ReverseGeocodeState.Success(res.reverseGeocode)
            } catch(e: ApolloException) {
                ReverseGeocodeState.Error(e.message)
            }
        }
    }

    fun reportTripStatus(status: TripStatus) {
        if (reportTripStatusState !is ReportTripStatusState.Loading) {
            reportTripStatusState = ReportTripStatusState.Loading
            viewModelScope.launch {
                reportTripStatusState = try {
                    val res = tripRepository.reportTripStatus(tripUiState.value.id, status).dataOrThrow()
                    ReportTripStatusState.Success(res.reportTripStatus)
                } catch(e: ApolloException) {
                    e.printStackTrace()
                    ReportTripStatusState.Error(e.message)
                }
            }
        }
    }
}

interface GetCourierTripState {
    data class Success(val trip: GetTripQuery.GetTripDetails?): GetCourierTripState
    data object Loading: GetCourierTripState
    data class Error(val message: String?): GetCourierTripState
}

interface ReverseGeocodeState {
    data class Success(val geocode: ReverseGeocodeQuery.ReverseGeocode?): ReverseGeocodeState
    data object Loading: ReverseGeocodeState
    data class Error(val message: String?): ReverseGeocodeState
}

interface ReportTripStatusState {
    data class Success(val tripStatus: Boolean): ReportTripStatusState
    data object Loading: ReportTripStatusState
    data class Error(val message: String?): ReportTripStatusState
}