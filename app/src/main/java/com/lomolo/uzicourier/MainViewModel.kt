package com.lomolo.uzicourier

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.lomolo.uzicourier.common.countryPhoneCode
import com.lomolo.uzicourier.compose.signin.SessionViewModel
import com.lomolo.uzicourier.model.Session
import com.lomolo.uzicourier.network.UziGqlApiInterface
import com.lomolo.uzicourier.network.UziRestApiServiceInterface
import com.lomolo.uzicourier.repository.CourierInterface
import com.lomolo.uzicourier.repository.SessionInterface
import com.lomolo.uzicourier.repository.SessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class MainViewModel(
    private val uziRestApiService: UziRestApiServiceInterface,
    private val uziGqlApiService: UziGqlApiInterface,
    private val courierRepository: CourierInterface,
    private val sessionRepository: SessionInterface
): ViewModel() {
    val sessionUiState: StateFlow<Session> = sessionRepository
        .getSession()
        .filterNotNull()
        .map {
            if (it.isNotEmpty()) {
                Session(
                    token = it[0].token,
                    id = it[0].id,
                    courierStatus = it[0].courierStatus,
                    phone = it[0].phone,
                    isCourier = it[0].isCourier,
                    onboarding = it[0].onboarding
                )
            } else {
                Session()
            }
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = Session(),
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS)
        )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
    private val _deviceDetails: MutableStateFlow<DeviceDetails> = MutableStateFlow(DeviceDetails())
    val deviceDetailsUiState = _deviceDetails.asStateFlow()

    var deviceDetailsState: DeviceDetailsUiState by mutableStateOf(DeviceDetailsUiState.Loading)
        private set

    var setCourierStatusState: SetCourierStatusState by mutableStateOf(SetCourierStatusState.Success)
        private set

    fun setDeviceLocation(gps: LatLng) {
        _deviceDetails.update {
            it.copy(gps = gps)
        }
        if (sessionUiState.value.token.isNotBlank()) {
            viewModelScope.launch(Dispatchers.IO) {
                if (gps.latitude != 0.0 && gps.longitude != 0.0) {
                    delay(4000L)
                    courierRepository.trackCourierGps(gps)
                }
            }
        }
    }

    fun getIpinfo() {
        viewModelScope.launch {
            deviceDetailsState = DeviceDetailsUiState.Loading
            try {
                val response = uziRestApiService.getIpinfo()
                val ipGps = response.location.split(",")
                _deviceDetails.update {
                    deviceDetailsState = DeviceDetailsUiState.Success
                    it.copy(
                        gps = LatLng(ipGps[0].toDouble(), ipGps[1].toDouble()),
                        country = response.country,
                        countryFlag = response.countryFlag,
                        countryPhoneCode = countryPhoneCode[response.country]!!
                    )
                }
            } catch (e: Throwable) {
                deviceDetailsState = DeviceDetailsUiState.Error(e.localizedMessage)
                e.printStackTrace()
            }
        }
    }

    fun setHasGps(b: Boolean) {
        _deviceDetails.update {
            it.copy(hasGps = b)
        }
    }

    fun setMapLoaded(l: Boolean) {
        _deviceDetails.update {
            it.copy(mapLoaded = l)
        }
    }

    fun setCourierStatus(status: String, cb: () -> Unit = {}) {
        if (setCourierStatusState !is SetCourierStatusState.Loading) {
            setCourierStatusState = SetCourierStatusState.Loading
            viewModelScope.launch {
                setCourierStatusState = try {
                    uziGqlApiService.setCourierStatus(status)
                    SetCourierStatusState.Success.also { cb() }
                } catch (e: IOException) {
                    SetCourierStatusState.Error(e.message)
                }
            }
        }
    }

    init {
        getIpinfo()
    }
}

data class DeviceDetails(
    val gps: LatLng = LatLng(0.0, 0.0),
    val country: String = "",
    val countryFlag: String = "",
    val countryPhoneCode: String = "",
    val hasGps: Boolean = false,
    val mapLoaded: Boolean = false
)

interface DeviceDetailsUiState {
    data object Loading: DeviceDetailsUiState
    data object Success: DeviceDetailsUiState
    data class Error(val message: String?): DeviceDetailsUiState
}

interface SetCourierStatusState {
    data object Success: SetCourierStatusState
    data object Loading: SetCourierStatusState
    data class Error(val message: String?): SetCourierStatusState
}