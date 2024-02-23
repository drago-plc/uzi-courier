package com.lomolo.uzicourier.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class Trip (
    @PrimaryKey val id: String = "",
    val status: String = TripStatus.CREATE.toString()
)

enum class TripStatus {
    CANCELLED,
    CREATE,
    COURIER_ASSIGNED,
    COMPLETE,
}