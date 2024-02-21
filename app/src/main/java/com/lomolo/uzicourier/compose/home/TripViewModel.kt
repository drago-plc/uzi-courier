package com.lomolo.uzicourier.compose.home

import androidx.lifecycle.ViewModel
import com.lomolo.uzicourier.repository.TripInterface

class TripViewModel(
    private val tripRepository: TripInterface
): ViewModel() {
}