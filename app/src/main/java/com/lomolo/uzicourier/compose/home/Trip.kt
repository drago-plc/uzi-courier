package com.lomolo.uzicourier.compose.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import com.google.maps.android.compose.CameraPositionState
import com.lomolo.uzicourier.compose.loader.Loader

@Composable
internal fun TripScreen(
    modifier: Modifier = Modifier,
    tripViewModel: TripViewModel,
    cameraPositionState: CameraPositionState,
    mapLoaded: Boolean
) {
    LaunchedEffect(Unit) { tripViewModel.getCourierAssignedTrip() }

    Box(modifier = modifier) {
        when(val s = tripViewModel.getCourierTripState) {
            GetCourierTripState.Loading -> {
                Loader(
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            is GetCourierTripState.Success -> {
                if (s.trip != null) {
                    LaunchedEffect(Unit) {
                        if (s.trip.start_location != null && mapLoaded) {
                            cameraPositionState.animate(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        s.trip.start_location.lat,
                                        s.trip.start_location.lng
                                    ), 17f
                                ),
                                1000
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(8.dp)
                    ) {
                        Text(
                            "Your trip is ready. Proceed to pickup",
                            style = MaterialTheme.typography.labelSmall
                        )

                        Button(
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = MaterialTheme.shapes.small,
                            onClick = { /*TODO*/ }
                        ) {
                            Text(
                                "Arrive",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }
        }
    }
}