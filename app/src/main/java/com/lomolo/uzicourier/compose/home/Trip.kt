package com.lomolo.uzicourier.compose.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lomolo.uzicourier.GetTripQuery
import com.lomolo.uzicourier.compose.loader.Loader
import com.lomolo.uzicourier.model.Trip
import com.lomolo.uzicourier.type.TripStatus
import java.text.NumberFormat

@Composable
internal fun TripScreen(
    modifier: Modifier = Modifier,
    tripViewModel: TripViewModel,
    trip: GetTripQuery.GetTripDetails,
    assignment: Trip
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.background,
                    MaterialTheme.shapes.small
                )
                .padding(8.dp)
        ) {
            when (assignment.status) {
                TripStatus.COURIER_ASSIGNED.toString(), TripStatus.COURIER_ARRIVING.toString(), TripStatus.COURIER_EN_ROUTE.toString() -> {
                    Text(
                        "Your trip is ready.",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Box(
                        Modifier
                            .padding(8.dp)
                    ) {
                        when (val g = tripViewModel.reverseGeocodeState) {
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
                                                    trip.recipient.name,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    modifier = Modifier.padding(
                                                        top = 8.dp,
                                                        bottom = 8.dp
                                                    )
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
                                            if (!trip.recipient.building_name.isNullOrBlank() || !trip.recipient.unit_name.isNullOrBlank()) {
                                                Row {
                                                    Icon(
                                                        Icons.TwoTone.Home,
                                                        contentDescription = null,
                                                        modifier = Modifier
                                                            .size(32.dp)
                                                    )
                                                    Text(
                                                        "${trip.recipient.building_name} ${trip.recipient.unit_name}",
                                                        style = MaterialTheme.typography.bodyLarge
                                                    )
                                                }
                                            }
                                            if (trip.recipient.trip_note.isNotBlank()) {
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
                                                    "Trip Cost KES ${
                                                        NumberFormat.getNumberInstance()
                                                            .format(trip.cost)
                                                    }",
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

                    when (assignment.status) {
                        TripStatus.COURIER_ASSIGNED.toString() -> {
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                shape = MaterialTheme.shapes.small,
                                onClick = {
                                    tripViewModel.reportTripStatus(TripStatus.COURIER_ARRIVING)
                                }
                            ) {
                                if (tripViewModel.reportTripStatusState is ReportTripStatusState.Loading) {
                                    Loader()
                                } else {
                                    Text(
                                        "Arrive",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                        }

                        TripStatus.COURIER_ARRIVING.toString() -> {
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                shape = MaterialTheme.shapes.small,
                                onClick = {
                                    tripViewModel.reportTripStatus(TripStatus.COURIER_EN_ROUTE)
                                }
                            ) {
                                if (tripViewModel.reportTripStatusState is ReportTripStatusState.Loading) {
                                    Loader()
                                } else {
                                    Text(
                                        "Start trip",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                        }

                        TripStatus.COURIER_EN_ROUTE.toString() -> {
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                shape = MaterialTheme.shapes.small,
                                onClick = {
                                    tripViewModel.reportTripStatus(TripStatus.COMPLETE)
                                }
                            ) {
                                if (tripViewModel.reportTripStatusState is ReportTripStatusState.Loading) {
                                    Loader()
                                } else {
                                    Text(
                                        "End trip",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                        }
                        else -> {}
                    }
                }
                TripStatus.COMPLETE.toString() -> {
                    Box(
                        Modifier
                            .padding(8.dp)
                    ) {
                        Column {
                            Text(
                                "Get paid for the trip",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.labelMedium
                            )
                            Spacer(modifier = Modifier.size(16.dp))
                            Text(
                                "KES ${NumberFormat.getNumberInstance().format(trip.cost)}",
                                style = MaterialTheme.typography.labelLarge,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(12.dp)
                            )
                            Spacer(modifier = Modifier.size(12.dp))
                            Button(
                                onClick = {
                                    tripViewModel.clearTrips()
                                },
                                modifier = Modifier
                                    .fillMaxWidth(),
                                shape = MaterialTheme.shapes.small
                            ) {
                               Text(
                                   "Done",
                                   style = MaterialTheme.typography.labelSmall
                               )
                            }
                        }
                    }
                }
                else -> {}
            }
        }
    }
}