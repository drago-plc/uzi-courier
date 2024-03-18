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
    assignment: Trip,
    arrived: Boolean
) {
    val tripDistance = { it: Int -> if (it > 1000) "${it/1000}KM" else "${it}M"}
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
                TripStatus.COURIER_ASSIGNED.toString() -> {
                    Column {
                        Text(
                            "Your trip is ready. Confirm to take it.",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                tripViewModel.reportTripStatus(TripStatus.COURIER_ARRIVING)
                            },
                            shape = MaterialTheme.shapes.small
                        ) {
                            if (tripViewModel.reportTripStatusState is ReportTripStatusState.Loading) {
                                Loader()
                            } else {
                                Text(
                                    "KES ${
                                        NumberFormat.getNumberInstance().format(trip.cost)
                                    } / ${tripDistance(trip.route!!.distance)}",
                                    style = MaterialTheme.typography.titleMedium,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }

                        }
                    }
                }
                TripStatus.COURIER_ARRIVING.toString() -> {
                    Column {
                        Text(
                            if (arrived) "Start trip. Proceed to your drop-off location" else "Heading to your pick-up location",
                            style = MaterialTheme.typography.titleMedium
                        )
                        if (arrived) {
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
                                        "En-Route",
                                        style = MaterialTheme.typography.titleMedium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(12.dp)
                                    )
                                }
                            }
                        }
                    }
                }
                TripStatus.COURIER_EN_ROUTE.toString() -> {
                    Text(
                        if (arrived) "You have arrived" else "Heading to your drop-off location",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Column(
                        Modifier
                            .padding(8.dp)
                    ) {
                        Text(
                            "Heading to your drop-off location",
                            style = MaterialTheme.typography.titleMedium
                        )
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
                        }
                        if (arrived) {
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
                                        style = MaterialTheme.typography.titleMedium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(12.dp)
                                    )
                                }
                            }
                        }
                    }
                }
                TripStatus.COMPLETE.toString() -> {
                    Column(
                        Modifier
                            .padding(8.dp)
                    ) {
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
        }
    }
}