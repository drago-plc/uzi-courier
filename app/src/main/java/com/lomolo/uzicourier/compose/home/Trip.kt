package com.lomolo.uzicourier.compose.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Call
import androidx.compose.material.icons.twotone.Home
import androidx.compose.material.icons.twotone.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
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
import java.text.NumberFormat

@Composable
internal fun TripScreen(
    modifier: Modifier = Modifier,
    tripViewModel: TripViewModel,
    cameraPositionState: CameraPositionState,
    mapLoaded: Boolean
) {
    LaunchedEffect(Unit) { tripViewModel.getCourierAssignedTrip() }
    var polyline: List<LatLng> = listOf()

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
                        if (s.trip.route != null) polyline = PolyUtil.decode(s.trip.route.polyline)
                        if (polyline.isNotEmpty()) tripViewModel.reverseGeocode(polyline.last())
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(8.dp)
                    ) {
                        Text(
                            "Your trip is ready. Proceed to pickup",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Box(Modifier
                            .padding(8.dp)
                        ) {
                            when(val g = tripViewModel.reverseGeocodeState) {
                                is ReverseGeocodeState.Success -> {
                                    if (g.geocode != null) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .padding(16.dp)
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text(
                                                        s.trip.recipient.name,
                                                        style = MaterialTheme.typography.labelSmall,
                                                        modifier = Modifier.padding(top=8.dp, bottom=8.dp)
                                                    )
                                                    IconButton(onClick = { /*TODO*/ }) {
                                                       Icon(
                                                           Icons.TwoTone.Call,
                                                           contentDescription = null,
                                                           modifier = Modifier.size(20.dp)
                                                       )
                                                    }
                                                }
                                                Row {
                                                    Icon(
                                                        Icons.TwoTone.LocationOn,
                                                        contentDescription = null,
                                                        modifier = Modifier
                                                            .size(32.dp)
                                                    )
                                                    Text(
                                                        g.geocode.formattedAddress,
                                                        style = MaterialTheme.typography.bodyLarge
                                                    )
                                                }
                                                if (!s.trip.recipient.building_name.isNullOrBlank() || !s.trip.recipient.unit_name.isNullOrBlank()) {
                                                    Row {
                                                        Icon(
                                                            Icons.TwoTone.Home,
                                                            contentDescription = null,
                                                            modifier = Modifier
                                                                .size(32.dp)
                                                        )
                                                        Text(
                                                            "${s.trip.recipient.building_name} ${s.trip.recipient.unit_name}",
                                                            style = MaterialTheme.typography.bodyLarge
                                                        )
                                                    }
                                                }
                                                if (s.trip.recipient.trip_note.isNotBlank()) {
                                                    Spacer(modifier = Modifier.size(16.dp))
                                                    SuggestionChip(
                                                        onClick = { /*TODO*/ },
                                                        label = {
                                                            Text(
                                                                text = "Trip note",
                                                                style = MaterialTheme.typography.labelSmall
                                                            )
                                                        }
                                                    )
                                                }
                                                Spacer(modifier = Modifier.size(16.dp))
                                                Box {
                                                    Text(
                                                        "Trip Cost KES ${NumberFormat.getNumberInstance().format(s.trip.cost)}",
                                                        style = MaterialTheme.typography.bodyLarge
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                ReverseGeocodeState.Loading -> {
                                    Loader()
                                }
                            }
                        }

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